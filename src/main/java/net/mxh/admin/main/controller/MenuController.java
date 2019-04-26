package net.mxh.admin.main.controller;

import java.util.*;

import net.mxh.admin.main.bean.MenuGroup;
import net.mxh.entity.Admin;
import net.mxh.entity.Role;
import net.mxh.service.AdminService;
import net.mxh.service.RoleService;
import net.mxh.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.admin.main.bean.CommonSelect;
import net.mxh.entity.Menu;
import net.mxh.service.MenuService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.JsonResultData;
import net.mxh.util.ValidateUtil;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping (value = "admin/menu")
public class MenuController {

    private static int pageSize = 10;

    private static int dialogPageSize = 5;

    private static List<CommonSelect> selectList = new ArrayList<>();

    static {
        selectList.add(new CommonSelect(0 , "全部"));
        selectList.add(new CommonSelect(1 , "菜单名称"));
    }

    @Autowired
    private MenuService menuService;

    //用于admin的菜单重建缓存,在save方法中使用
    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    /**
     * @author bigduu
     * @QQ 776273900
     */
    private void reCasheMenu(HttpServletRequest request) {
        ///////////////////////////////////
        //此处用于admin重建菜单缓存
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        Role role = roleService.findById(admin.getRoleId());
        List<Menu> menuTow = new ArrayList<>();
        List<Menu> menus = new ArrayList<>();
        menus = menuService.findByMenuLevel(CategoryUtil.MENULEVEL.ONE);
        if ("admin".equals(role.getRoleCode())) {
            menuTow = menuService.findByMenuLevel(CategoryUtil.MENULEVEL.TOW);
        } else {
            menuTow = menuService.findByRoleId(role.getId() , CategoryUtil.MENULEVEL.TOW);
        }
        List<MenuGroup> menuGroups = new ArrayList<MenuGroup>();
        Map<Long, MenuGroup> groupMap = new HashMap<>();
        //NMenu用于对admin的重新复制
        for (Menu NMenu : menus) {
            MenuGroup group = new MenuGroup();
            group.setMenuCss(NMenu.getIcon());
            group.setName(NMenu.getMenuName());
            groupMap.put(NMenu.getId() , group);
            menuGroups.add(group);
        }
        Set<String> menuUrls = new HashSet<String>();
        for (Menu NMenu : menuTow) {
            Long parentId = NMenu.getParentId();
            MenuGroup group = groupMap.get(parentId);
            if (group != null) {
                menuUrls.add(NMenu.getLinkUrl());
                group.getMenus().add(NMenu);
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
        request.getSession().setAttribute("admin" , admin);

    }

    /**
     * 显示菜单列表
     * @param page
     * @param selectId
     * @param searchName
     * @return
     */
    @RequestMapping (value = "/menuList", method = RequestMethod.POST)
    public ModelAndView menuList(Integer page , String selectId , String searchName) {
        if (selectId == null) {
            selectId = "0";
            searchName = "";
        }
        searchName = searchName.trim();

        List<Menu> menuList = null;
        long total = 0;
        if ("0".equals(selectId)) {
            menuList = menuService.findAll(page , pageSize);
            total = menuService.count();
        } else if ("1".equals(selectId)) {
            menuList = menuService.findByMenuName(searchName , page , pageSize);
            total = menuService.countByMenuName(searchName);

        }
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        ModelAndView modelAndView = new ModelAndView("/menu/menuList");
        modelAndView.addObject("menuList" , menuList);
        modelAndView.addObject("selectId" , selectId);
        modelAndView.addObject("selectList" , selectList);
        modelAndView.addObject("searchName" , searchName);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        return modelAndView;
    }

    @RequestMapping (value = "/menuEdit", method = RequestMethod.POST)
    public ModelAndView menuEdit(Integer page , Long menuId) {
        ModelAndView modelAndView = new ModelAndView("/menu/menuEdit");
        modelAndView.addObject("menuId" , menuId);
        modelAndView.addObject("page" , page);
        if (0 != menuId) {
            Menu menu = menuService.findById(menuId);
            if (menu.getMenuLevel() != CategoryUtil.MENULEVEL.ONE) {
                Menu parentMenu = menuService.findById(menu.getParentId());
                if (parentMenu != null) {
                    menu.setParentName(parentMenu.getMenuName());
                }
            }
            modelAndView.addObject("menu" , menu);
        }
        return modelAndView;
    }

    /**
     * 保存菜单信息
     * @param request用于获取session信息
     * @return 返回成功
     */
    @RequestMapping (value = "/menuSave", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData menuSave(HttpServletRequest request , Menu menu) {
        JsonResultData result = JsonResultData.success();
        menu.setMenuName(menu.getMenuName().trim());
        menu.setLinkUrl(menu.getLinkUrl().trim());
        menu.setIcon(menu.getIcon().trim());
        if (!ValidateUtil.lengthBetween(menu.getMenuName() , 2 , 10)) {
            return result.turnError("菜单名称的长度必须是2-10位字符");
        }
        if (menu.getMenuLevel() == 1 && !ValidateUtil.lengthBetween(menu.getIcon() , 2 , 30)) {
            return result.turnError("菜单样式长度必须是2-30位字符");
        }
        if (menu.getMenuLevel() == 2 && !ValidateUtil.lengthBetween(menu.getLinkUrl() , 2 , 100)) {
            return result.turnError("菜单路径长度必须是2-100位字符");
        }
        if (menu.getMenuLevel() == 2 && null == menu.getParentId()) {
            return result.turnError("请选择父级菜单");
        }
        try {
            if (0 == menu.getId()) {
                menu.setId(null);
            }
            if (menu.getMenuLevel() == 1) {
                menu.setLinkUrl("");
                menu.setParentId(null);
            }
            if (menu.getMenuLevel() == 2) {
                menu.setIcon("");
            }
            List<Menu> allMenus = menuService.findAll(1 , Integer.MAX_VALUE);
            for (Menu allMenu : allMenus) {
                if (menu.getId() == null && allMenu.getMenuName().equals(menu.getMenuName())) {
                    return result.turnError("已经有相同名称的菜单，请换一个菜单名称");
                }
                if (allMenu.getMenuLevel() == 2 && menu.getId() == null && allMenu.getLinkUrl().equals(menu.getLinkUrl())) {
                    return result.turnError("已经有相同路径的菜单，请换一个菜单路径");
                }
            }
            menuService.saveOrUpdate(menu);
        } catch (Exception e) {
            e.printStackTrace();
            result.turnError("保存失败");
        }
        /**
         * @author bigduu
         */
        //重新缓存菜单数据
        reCasheMenu(request);

        return result;
    }

    @RequestMapping (value = "/menuParentList", method = RequestMethod.POST)
    public ModelAndView menuParentList(Integer page , Integer parentLevel) {
        List<Menu> menuList = menuService.findByMenuLevel(parentLevel , page , dialogPageSize);
        long total = menuService.countByMenuLevel(parentLevel);
        total = (total / dialogPageSize == 0) ? (1) : (total % dialogPageSize == 0 ? total / dialogPageSize : total / dialogPageSize + 1);
        ModelAndView modelAndView = new ModelAndView("/menu/menuParentList");
        modelAndView.addObject("menuList" , menuList);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("parentLevel" , parentLevel);
        return modelAndView;
    }

    @RequestMapping (value = "/menuDelete", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData menuDelete(HttpServletRequest request,Long menuId) {
        JsonResultData result = JsonResultData.success();

        try {
            Menu menu = menuService.findById(menuId);
            if (menu.getMenuLevel() == CategoryUtil.MENULEVEL.ONE) {
                Long count = menuService.countByParentId(menuId);
                if (count > 0) {
                    return result.turnError("此一级菜单还有子菜单，不能删除，请先删除子菜单");
                }
            }
            menuService.delete(menu);
        } catch (Exception e) {
            e.printStackTrace();
            result.turnError("删除失败");
        }
        //重新缓存菜单数据
        reCasheMenu(request);
        return result;
    }
}
