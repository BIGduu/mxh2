package net.mxh.admin.main.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.admin.main.bean.CommonSearch;
import net.mxh.admin.main.bean.OrderStat;
import net.mxh.entity.Admin;
import net.mxh.entity.Building;
import net.mxh.entity.Merchandise;
import net.mxh.entity.User;
import net.mxh.service.BuildingService;
import net.mxh.service.MerchandiseService;
import net.mxh.service.OrderInfoService;
import net.mxh.service.UserService;
import net.mxh.util.CategoryUtil;

@Controller
@RequestMapping(value = "admin/data")
public class DataController {
	
	@Autowired
	private OrderInfoService orderInfoService;
	
	@Autowired
	private MerchandiseService merchandiseService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BuildingService buildingService;
	
	/**
	 * 数据统计
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/datastat", method = RequestMethod.POST)
	public ModelAndView datastat(@RequestParam Map<String, String> param, HttpSession session) {
		Admin admin = (Admin)session.getAttribute("admin");
		if (null != admin.getStoresId()) {
			param.put("storesId", admin.getStoresId().toString());
		}
		
		List<User> users = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.ONE);
		List<CommonSearch> managers = new ArrayList<>();
		for (User user : users) {
			managers.add(new CommonSearch(user.getId(), user.getUsername()));
		}
		
		List<Building> buildingList = buildingService.findAll();
		List<CommonSearch> buildings = new ArrayList<>();
		for (Building building : buildingList) {
			buildings.add(new CommonSearch(building.getId(), building.getBuildingName()));
		}
		
		List<Merchandise> mdlist = merchandiseService.findByIsUse(1, null);
		List<OrderStat> list = orderInfoService.findCountList(param);
		
		OrderStat orderstat = new OrderStat();
		
		Map<String, OrderStat> merchandisetmp = new HashMap<String, OrderStat>();
		Map<String, String> mertchmap = new HashMap<String, String>();
		mdlist.forEach(m -> {
			OrderStat stattmp = new OrderStat();
			merchandisetmp.put(m.getId().toString(), stattmp);
			mertchmap.put(m.getId().toString(), m.getMerchandiseName());
		});
		orderstat.setMerchandise(new HashMap<String, OrderStat>(merchandisetmp));
		
		list.forEach(oi -> {
			
			Integer rowspan = orderstat.getRowspan();
			orderstat.setRowspan(rowspan + 1);
			
			BigDecimal oiallprice = oi.getAllPrice();
			// Integer number = oi.getNumber();
			
			BigDecimal allprice = orderstat.getAllPrice();
			orderstat.setAllPrice(allprice.add(oiallprice));
			
			Map<String, OrderStat> merch = orderstat.getMerchandise();
			OrderStat mstat = merch.get(oi.getMerchandiseId());
			mstat.setNumber(mstat.getNumber().add(oi.getNumber()));
			merch.put(oi.getMerchandiseId(), mstat);
			orderstat.setMerchandise(merch);
			
			Map<String, OrderStat> child1 = orderstat.getChild();
			OrderStat stat1 = child1.get(oi.getStoresId());
			
			if (stat1 == null) {
				OrderStat stat4 = new OrderStat(oi);
				Map<String, OrderStat> merchandisetmp4 = new HashMap<String, OrderStat>();
				mdlist.forEach(m -> {
					OrderStat stattmp = new OrderStat();
					merchandisetmp4.put(m.getId().toString(), stattmp);
				});
				// merchandisetmp4.putAll(merchandisetmp);
				merchandisetmp4.put(oi.getMerchandiseId(), new OrderStat(oi));
				stat4.setMerchandise(merchandisetmp4);
				Map<String, OrderStat> child4 = new HashMap<String, OrderStat>();
				child4.put(oi.getModificationTime(), stat4);
				
				OrderStat stat3 = new OrderStat(oi);
				Map<String, OrderStat> merchandisetmp3 = new HashMap<String, OrderStat>();
				mdlist.forEach(m -> {
					OrderStat stattmp = new OrderStat();
					merchandisetmp3.put(m.getId().toString(), stattmp);
				});
				// merchandisetmp3.putAll(merchandisetmp);
				merchandisetmp3.put(oi.getMerchandiseId(), new OrderStat(oi));
				stat3.setMerchandise(merchandisetmp3);
				stat3.setChild(child4);
				Map<String, OrderStat> child3 = new HashMap<String, OrderStat>();
				child3.put(oi.getReceivingBuildingId(), stat3);
				
				OrderStat stat2 = new OrderStat(oi);
				Map<String, OrderStat> merchandisetmp2 = new HashMap<String, OrderStat>();
				
				mdlist.forEach(m -> {
					OrderStat stattmp = new OrderStat();
					merchandisetmp2.put(m.getId().toString(), stattmp);
				});
				// merchandisetmp2.putAll(merchandisetmp);
				merchandisetmp2.put(oi.getMerchandiseId(), new OrderStat(oi));
				stat2.setMerchandise(merchandisetmp2);
				stat2.setChild(child3);
				Map<String, OrderStat> child2 = new HashMap<String, OrderStat>();
				child2.put(oi.getManagerId(), stat2);
				
				stat1 = new OrderStat(oi);
				Map<String, OrderStat> merchandisetmp1 = new HashMap<String, OrderStat>();
				
				mdlist.forEach(m -> {
					OrderStat stattmp = new OrderStat();
					merchandisetmp1.put(m.getId().toString(), stattmp);
				});
				// merchandisetmp1.putAll(merchandisetmp);
				merchandisetmp1.put(oi.getMerchandiseId(), new OrderStat(oi));
				stat1.setMerchandise(merchandisetmp1);
				stat1.setChild(child2);
				child1.put(oi.getStoresId(), stat1);
				
				orderstat.setChild(child1);
				
			}
			else {
				Integer rowspan1 = stat1.getRowspan();
				stat1.setRowspan(rowspan1 + 1);
				
				BigDecimal allprice1 = stat1.getAllPrice();
				stat1.setAllPrice(allprice1.add(oiallprice));
				
				Map<String, OrderStat> merch1 = stat1.getMerchandise();
				OrderStat mstat1 = merch1.get(oi.getMerchandiseId());
				// Integer mnumber1 = mstat1.getNumber();
				// mstat1.setNumber(mnumber1 + number);
				System.out.println(mstat1.getNumber() + " :mstat1: " + oi.getNumber());
				mstat1.setNumber(mstat1.getNumber().add(oi.getNumber()));
				System.out.println(mstat1.getNumber() + " :mstat1: " + oi.getNumber());
				merch1.put(oi.getMerchandiseId(), mstat1);
				stat1.setMerchandise(merch1);
				System.out.println("merch1:" + merch1.toString());
				
				Map<String, OrderStat> child2 = stat1.getChild();
				OrderStat stat2 = child2.get(oi.getManagerId());
				
				if (stat2 == null) {
					
					OrderStat stat4 = new OrderStat(oi);
					
					Map<String, OrderStat> merchandisetmp4 = new HashMap<String, OrderStat>();
					// merchandisetmp4.putAll(merchandisetmp);
					mdlist.forEach(m -> {
						OrderStat stattmp = new OrderStat();
						merchandisetmp4.put(m.getId().toString(), stattmp);
					});
					merchandisetmp4.put(oi.getMerchandiseId(), new OrderStat(oi));
					stat4.setMerchandise(merchandisetmp4);
					
					Map<String, OrderStat> child4 = new HashMap<String, OrderStat>();
					child4.put(oi.getModificationTime(), stat4);
					
					OrderStat stat3 = new OrderStat(oi);
					
					Map<String, OrderStat> merchandisetmp3 = new HashMap<String, OrderStat>();
					mdlist.forEach(m -> {
						OrderStat stattmp = new OrderStat();
						merchandisetmp3.put(m.getId().toString(), stattmp);
					});
					// merchandisetmp3.putAll(merchandisetmp);
					merchandisetmp3.put(oi.getMerchandiseId(), new OrderStat(oi));
					stat3.setMerchandise(merchandisetmp3);
					
					stat3.setChild(child4);
					Map<String, OrderStat> child3 = new HashMap<String, OrderStat>();
					child3.put(oi.getReceivingBuildingId(), stat3);
					
					stat2 = new OrderStat(oi);
					
					Map<String, OrderStat> merchandisetmp2 = new HashMap<String, OrderStat>();
					mdlist.forEach(m -> {
						OrderStat stattmp = new OrderStat();
						merchandisetmp2.put(m.getId().toString(), stattmp);
					});
					// merchandisetmp2.putAll(merchandisetmp);
					merchandisetmp2.put(oi.getMerchandiseId(), new OrderStat(oi));
					stat2.setMerchandise(merchandisetmp2);
					
					stat2.setChild(child3);
					child2.put(oi.getManagerId(), stat2);
					
					stat1.setChild(child2);
					child1.put(oi.getStoresId(), stat1);
					
					orderstat.setChild(child1);
					
				}
				else {
					
					Integer rowspan2 = stat2.getRowspan();
					stat2.setRowspan(rowspan2 + 1);
					
					BigDecimal allprice2 = stat2.getAllPrice();
					stat2.setAllPrice(allprice2.add(oiallprice));
					
					Map<String, OrderStat> merch2 = stat2.getMerchandise();
					OrderStat mstat2 = merch2.get(oi.getMerchandiseId());
					// Integer mnumber2 = mstat2.getNumber();
					// mstat2.setNumber(mnumber2 + number);
					// System.out.println(mstat2.getNumber() + " :mstat2: " + oi.getNumber());
					mstat2.setNumber(mstat2.getNumber().add(oi.getNumber()));
					// System.out.println(mstat2.getNumber() + " :mstat2: " + oi.getNumber());
					merch2.put(oi.getMerchandiseId(), mstat2);
					// System.out.println("merch2:" + merch2.toString());
					stat2.setMerchandise(merch2);
					
					Map<String, OrderStat> child3 = stat2.getChild();
					OrderStat stat3 = child3.get(oi.getReceivingBuildingId());
					
					if (stat3 == null) {
						merchandisetmp.put(oi.getMerchandiseId(), new OrderStat(oi));
						
						OrderStat stat4 = new OrderStat(oi);
						
						Map<String, OrderStat> merchandisetmp4 = new HashMap<String, OrderStat>();
						mdlist.forEach(m -> {
							OrderStat stattmp = new OrderStat();
							merchandisetmp4.put(m.getId().toString(), stattmp);
						});
						// merchandisetmp4.putAll(merchandisetmp);
						merchandisetmp4.put(oi.getMerchandiseId(), new OrderStat(oi));
						stat4.setMerchandise(merchandisetmp4);
						
						Map<String, OrderStat> child4 = new HashMap<String, OrderStat>();
						child4.put(oi.getModificationTime(), stat4);
						
						stat3 = new OrderStat(oi);
						
						Map<String, OrderStat> merchandisetmp3 = new HashMap<String, OrderStat>();
						mdlist.forEach(m -> {
							OrderStat stattmp = new OrderStat();
							merchandisetmp3.put(m.getId().toString(), stattmp);
						});
						// merchandisetmp3.putAll(merchandisetmp);
						merchandisetmp3.put(oi.getMerchandiseId(), new OrderStat(oi));
						stat3.setMerchandise(merchandisetmp3);
						
						stat3.setChild(child4);
						child3.put(oi.getReceivingBuildingId(), stat3);
						Integer rowspan3 = stat3.getRowspan();
						stat3.setRowspan(rowspan3 + 1);
						
						stat2.setChild(child3);
						child2.put(oi.getManagerId(), stat2);
						
						stat1.setChild(child2);
						child1.put(oi.getStoresId(), stat1);
						
						orderstat.setChild(child1);
						
					}
					else {
						Integer rowspan3 = stat3.getRowspan();
						stat3.setRowspan(rowspan3 + 1);
						
						BigDecimal allprice3 = stat3.getAllPrice();
						stat3.setAllPrice(allprice3.add(oiallprice));
						
						Map<String, OrderStat> merch3 = stat3.getMerchandise();
						OrderStat mstat3 = merch3.get(oi.getMerchandiseId());
						// Integer mnumber3 = mstat3.getNumber();
						// mstat3.setNumber(mnumber3 + number);
						// System.out.println(mstat3.getNumber() + " :mstat3: " + oi.getNumber());
						mstat3.setNumber(mstat3.getNumber().add(oi.getNumber()));
						// System.out.println(mstat3.getNumber() + " :mstat3: " + oi.getNumber());
						merch3.put(oi.getMerchandiseId(), mstat3);
						stat3.setMerchandise(merch3);
						
						Map<String, OrderStat> child4 = stat3.getChild();
						OrderStat stat4 = child4.get(oi.getModificationTime());
						if (stat4 == null) {
							merchandisetmp.put(oi.getMerchandiseId(), new OrderStat(oi));
							
							stat4 = new OrderStat(oi);
							
							Map<String, OrderStat> merchandisetmp4 = new HashMap<String, OrderStat>();
							mdlist.forEach(m -> {
								OrderStat stattmp = new OrderStat();
								merchandisetmp4.put(m.getId().toString(), stattmp);
							});
							// merchandisetmp4.putAll(merchandisetmp);
							merchandisetmp4.put(oi.getMerchandiseId(), new OrderStat(oi));
							stat4.setMerchandise(merchandisetmp4);
							
							child4.put(oi.getModificationTime(), stat4);
							
							stat3.setChild(child4);
							child3.put(oi.getReceivingBuildingId(), stat3);
							
							stat2.setChild(child3);
							child2.put(oi.getManagerId(), stat2);
							
							stat1.setChild(child2);
							child1.put(oi.getStoresId(), stat1);
							
							orderstat.setChild(child1);
						}
						else {
							// Integer rowspan4 = stat4.getRowspan();
							// stat4.setRowspan(rowspan4 + 1);
							
							BigDecimal allprice4 = stat4.getAllPrice();
							stat4.setAllPrice(allprice4.add(oiallprice));
							
							Map<String, OrderStat> merch4 = stat4.getMerchandise();
							OrderStat mstat4 = merch4.get(oi.getMerchandiseId());
							// Integer mnumber4 = mstat4.getNumber();
							// mstat4.setNumber(mnumber4 + number);
							// System.out.println(mstat4.getNumber() + " :mstat4: " + oi.getNumber());
							mstat4.setNumber(mstat4.getNumber().add(oi.getNumber()));
							// System.out.println(mstat4.getNumber() + " :mstat4: " + oi.getNumber());
							merch4.put(oi.getModificationTime(), mstat4);
							stat4.setMerchandise(merch4);
							
							stat3.setChild(child4);
							child3.put(oi.getReceivingBuildingId(), stat3);
							
							stat2.setChild(child3);
							child2.put(oi.getManagerId(), stat2);
							
							stat1.setChild(child2);
							child1.put(oi.getStoresId(), stat1);
							
							orderstat.setChild(child1);
							
						}
					}
					
				}
			}
		});
		ModelAndView modelAndView = new ModelAndView("/data/datastat");
		modelAndView.addObject("data", orderstat);
		modelAndView.addObject("mertchmap", mertchmap);
		modelAndView.addObject("managers", managers);
		modelAndView.addObject("buildings", buildings);
		return modelAndView;
	}
	
}