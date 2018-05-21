package com.bookStore.admin.product.handler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bookStore.admin.product.service.IAdminProductService;
import com.bookStore.commons.beans.Product;
import com.bookStore.commons.beans.ProductList;
import com.bookStore.utils.IdUtils;
import com.bookStore.utils.PageModel;

@Controller
@RequestMapping("/admin/products")
public class AdminProductHandler {

	@Autowired
	private IAdminProductService adminProductService;

	@RequestMapping("/listProduct.do")
	public String listProduct(Model model, @RequestParam(defaultValue = "1") Integer pageIndex) {
		// 分页参数封装到pageModel对象
		/*
		 * PageModel pageModel = new PageModel();
		 * pageModel.setPageIndex(pageIndex);
		 * 
		 * int count = adminProductService.findProductCount(name);
		 * pageModel.setRecordCount(count);
		 */

		List<Product> products = adminProductService.findProduct();
		model.addAttribute("products", products);
		return "/admin/products/list.jsp";
	}

	@RequestMapping("findProductByManyCondition.do")
	public String findProductByManyCondition(Model model, Product product, String minprice, String maxprice) {
		// System.out.println(product);
		// System.out.println(minprice+"-"+maxprice);

		List<Product> products = adminProductService.findProductByManyCondition(product, minprice, maxprice);
		model.addAttribute("products", products);
		model.addAttribute("minprice", minprice);
		model.addAttribute("maxprice", maxprice);

		return "/admin/products/list.jsp";
	}

	@RequestMapping("/addProduct.do")
	public String addProduct(Product product, MultipartFile upload, HttpSession session)
			throws IllegalStateException, IOException {
		// System.out.println(product);
		// System.out.println(upload);

		String path = session.getServletContext().getRealPath("/productImg");
		System.out.println(path);

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		String filename = IdUtils.getUUID() + "-" + upload.getOriginalFilename();
		String imgurl = path + File.separator + filename;
		upload.transferTo(new File(imgurl));

		product.setId(IdUtils.getUUID());
		product.setImgurl("/productImg/" + filename);

		adminProductService.addProduct(product);

		return "/admin/products/listProduct.do";
	}

	@RequestMapping("/findProductById.do")
	public String findProductById(String id, Model model) {

		Product product = adminProductService.findProductById(id);
		model.addAttribute("p", product);
		return "/admin/products/edit.jsp";
	}

	@RequestMapping("/editProduct.do")
	public String editProduct(Product product, MultipartFile upload, HttpSession session)
			throws IllegalStateException, IOException {

		if (!upload.isEmpty()) {
			// delete old image file
			Product target = adminProductService.findProductById(product.getId());
			File targetFile = new File(session.getServletContext().getRealPath("/") + target.getImgurl());
			if (targetFile.exists()) {
				targetFile.delete();
			}
			// upload new image file
			String path = session.getServletContext().getRealPath("/productImg");
			String fileName = IdUtils.getUUID() + "-" + upload.getOriginalFilename();
			upload.transferTo(new File(path + File.separator + fileName));
			product.setImgurl("/productImg/" + fileName);

		}
		adminProductService.editProduct(product);
		return "/admin/products/listProduct.do";

	}

	@RequestMapping("/deleteProduct.do")
	public String deleteProduct(HttpSession session, String id) {
		Product target = adminProductService.findProductById(id);
		File targetFile = new File(session.getServletContext().getRealPath("/") + target.getImgurl());
		if (targetFile.exists()) {
			targetFile.delete();
		}
		adminProductService.removeProduct(id);

		return "/admin/products/listProduct.do";
	}

	@RequestMapping("/download.do")
	public void download(HttpServletRequest request, HttpSession session, String year, String month,
			HttpServletResponse response) throws IOException {

		List<ProductList> plist = adminProductService.findProductList(year, month);
		System.out.println(plist);

		//download EXCEL Files xx.xls
		//filename
		String fileName = year + "年" + month + "月-销售榜单";
		//sheetname
		String sheetName = "月销售榜单";
		//title
		String titleName = year + "年" + month + "月-销售榜单";
		//columnname
		String[] columnName = {"商品名","销量"};
		int columnNumber = 2;
		
		String[][] dataList = new String[plist.size()][2];
		for(int i = 0;i<plist.size();i++){
			ProductList pl = plist.get(i);
			dataList[i][0] = pl.getName();
			dataList[i][1] = pl.getSalnum();
		}
		
		//point to one excel file
		HSSFWorkbook wb = new HSSFWorkbook();
		//point to one sheet in file
		HSSFSheet sheet = wb.createSheet(sheetName);
		//create sheet's first line
		HSSFRow row1 = sheet.createRow(0);
		//create first row's first column
		HSSFCell cell1 = row1.createCell(0);
		//merge cells
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnNumber-1));
		//insert value to cell
		cell1.setCellValue(titleName);
		//create second row
		HSSFRow row = sheet.createRow(1);
		
		for(int i = 0;i<columnNumber;i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName[i]);
		}
		
		//create data rows
		for(int i = 0;i<dataList.length;i++){
			row = sheet.createRow(i+2);
			HSSFCell datacell = null;
			for(int j = 0;j<columnNumber;j++){
				datacell = row.createCell(j);
				datacell.setCellValue(dataList[i][j]);
			}
			
		}
		
		//download Excel file
		String filename = fileName+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("content-Disposition", "attachment;fileName=" + processFileName(request, filename));
		OutputStream out = response.getOutputStream();
		//write data to file
		wb.write(out);
		
		//download CSV files
		/*String fileName = year + "年" + month + "月-销售榜单.csv";
		response.setContentType(session.getServletContext().getMimeType(fileName));
		response.setHeader("content-Disposition", "attachment;fileName=" + processFileName(request, fileName));

		response.setCharacterEncoding("gbk");

		PrintWriter out = response.getWriter();

		// insert into file
		out.println("name,salnum");
		for (int i = 0; i < plist.size(); i++) {
			ProductList pl = plist.get(i);
			out.println(pl.getName() + "," + pl.getSalnum());

		}
		out.flush();
		out.close();*/
	}

	public static String processFileName(HttpServletRequest request, String fileNames) {
		String codedfilename = null;
		try {
			String agent = request.getHeader("USER-AGENT");
			if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie

				String name = java.net.URLEncoder.encode(fileNames, "UTF8");

				codedfilename = name;
			} else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等

				codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codedfilename;
	}
}
