package net.mxh.admin.main.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.admin.main.bean.MenuGroup;
import net.mxh.entity.Admin;
import net.mxh.entity.Menu;
import net.mxh.entity.Role;
import net.mxh.service.AdminService;
import net.mxh.service.MenuService;
import net.mxh.service.RoleService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.JsonResultData;
import net.mxh.util.StringUtil;

@Controller
@RequestMapping(value = "admin")
public class AdminLoginController {
	
	private static final int WIDTH = 90;
	
	private static final int HEIGHT = 30;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
		if (request.getSession().getAttribute("admin") != null) {
			return new ModelAndView("redirect:/admin/main");
		}
		else {
			return new ModelAndView("/main/login");
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData login(HttpServletRequest request, String adminName, String password) {
		JsonResultData result = JsonResultData.success();
		Admin admin = adminService.findByAdminName(adminName);
		if (admin == null) {
			return result.turnError("用户不存在");
		}
		if (!password.trim().equals(admin.getPassword())) {
			System.out.println("收到的密码" + password.trim());
			System.out.println("原来的密码" + admin.getPassword());
			return result.turnError("密码错误");
		}
		request.getSession().removeAttribute("checkcode");
		
		Role role = roleService.findById(admin.getRoleId());
		List<Menu> menuTow = new ArrayList<>();
		List<Menu> menus = new ArrayList<>();
		menus = menuService.findByMenuLevel(CategoryUtil.MENULEVEL.ONE);
		if ("admin".equals(role.getRoleCode())) {
			menuTow = menuService.findByMenuLevel(CategoryUtil.MENULEVEL.TOW);
		}
		else {
			menuTow = menuService.findByRoleId(role.getId(), CategoryUtil.MENULEVEL.TOW);
		}
		List<MenuGroup> menuGroups = new ArrayList<MenuGroup>();
		Map<Long, MenuGroup> groupMap = new HashMap<>();
		for (Menu menu : menus) {
			MenuGroup group = new MenuGroup();
			group.setMenuCss(menu.getIcon());
			group.setName(menu.getMenuName());
			groupMap.put(menu.getId(), group);
			menuGroups.add(group);
		}
		Set<String> menuUrls = new HashSet<String>();
		for (Menu menu : menuTow) {
			Long parentId = menu.getParentId();
			MenuGroup group = groupMap.get(parentId);
			if (group != null) {
				menuUrls.add(menu.getLinkUrl());
				group.getMenus().add(menu);
			}
		}
		//吧数据库中空的菜单列表去除
		for (int i = menuGroups.size() - 1; i >= 0; i--) {
			if (menuGroups.get(i).getMenus().isEmpty()) {
				menuGroups.remove(i);
			}
		}
		//在admin对象中设置菜单分组和子菜单和菜单链接
		admin.setMenuGroups(menuGroups);
		admin.setMenuUrls(menuUrls);
		if (StringUtil.isNotEmpty(role.getStates())) {
			String[] statesList = role.getStates().split(",");
			Set<Integer> stateList = new HashSet<>();
			for (String state : statesList) {
				stateList.add(Integer.parseInt(state));
			}
			admin.setStateList(stateList);
		}
		
		if (StringUtil.isNotEmpty(role.getChecks())) {
			String[] checksList = role.getChecks().split(",");
			Set<Integer> checkList = new HashSet<>();
			for (String check : checksList) {
				checkList.add(Integer.parseInt(check));
			}
			admin.setCheckList(checkList);
		}
		request.getSession().setAttribute("admin", admin);
		return result;
	}
	
	@RequestMapping(value = "/imageValidate", method = RequestMethod.GET)
	public void imageValidate(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		Graphics g = image.getGraphics();
		
		// 1.设置背景色
		setBackGround(g);
		
		// 2.设置边框
		setBorder(g);
		
		// 3.画干扰线
		drawRandomLine(g);
		
		// 4.写随机数
		String random = drawRondowNum((Graphics2D)g);
		request.getSession().setAttribute("checkcode", random);
		
		// 5.图形写给浏览器
		response.setContentType("image/jpeg");
		// 发头控制浏览器不要缓存
		response.setDateHeader("expries", -1);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		ImageIO.write(image, "jpg", response.getOutputStream());
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request) {
		request.getSession().removeAttribute("admin");
		return new ModelAndView("/main/login");
	}
	
	@RequestMapping(value = "/forget", method = RequestMethod.GET)
	public ModelAndView forget(HttpServletRequest request) {
		return new ModelAndView("/main/forget");
	}
	
	private String drawRondowNum(Graphics2D g) {
		g.setColor(new Color(0x5C90D2));
		g.setFont(new Font("微软雅黑", Font.BOLD, 23));
		
		String base = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
		
		// 汉字区间[\u4e00-\u9fa5]
		StringBuffer sb = new StringBuffer();
		int x = 10;
		for (int i = 0; i < 4; i++) {
			String ch = base.charAt(new Random().nextInt(base.length())) + "";
			sb.append(ch);
			int degree = new Random().nextInt() % 30;
			g.rotate(degree * Math.PI / 180, x, 20);// 设置旋转的弧度
			g.drawString(ch, x, 25);
			g.rotate(-degree * Math.PI / 180, x, 20);
			x += 20;
		}
		return sb.toString();
	}
	
	private void drawRandomLine(Graphics g) {
		g.setColor(Color.GREEN);
		for (int i = 0; i < 5; i++) {
			int x1 = new Random().nextInt(WIDTH - 2) + 1;
			int y1 = new Random().nextInt(HEIGHT - 2) + 1;
			
			int x2 = new Random().nextInt(WIDTH - 2) + 1;
			int y2 = new Random().nextInt(HEIGHT - 2) + 1;
			
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	private void setBorder(Graphics g) {
		/* g.setColor(Color.BLUE); */
		g.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
	}
	
	private void setBackGround(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}
}
