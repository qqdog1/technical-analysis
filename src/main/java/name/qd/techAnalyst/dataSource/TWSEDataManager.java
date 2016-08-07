package name.qd.techAnalyst.dataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import name.qd.techAnalyst.util.FileConstUtil;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class TWSEDataManager {
	private String sFilePath;
	private TWSEDataPoller poller;
	private TWSEDataParser parser;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public TWSEDataManager(String sFilePath) {
		this.sFilePath = sFilePath;
		poller = new TWSEDataPoller(sFilePath);
		parser = new TWSEDataParser(sFilePath);
	}
	
	public void checkDateAndDownload(String sFrom, String sTo, String sProdId) throws ParseException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dateFrom = sdf.parse(sFrom);
		Date dateTo = sdf.parse(sTo);
		List<String> lstYearMonth = TimeUtil.getYearMonthBetween(dateFrom, dateTo);
		List<String> lstDate = TimeUtil.getDateBetween(dateFrom, dateTo);
		List<String> lstPOSTDate = TimeUtil.getPOSTDateBetween(dateFrom, dateTo);
		checkAndDownloadProdClosing(sFilePath, lstYearMonth, sProdId);
		checkAndDownloadDailyClosing(sFilePath, lstDate, lstPOSTDate);
	}
	
	private void checkAndDownloadProdClosing(String sFilePath, List<String> lst, String sProdId) throws IOException {
		for(String sYearMonth : lst) {
			File file = new File(FileConstUtil.getProdClosingFilePath(sFilePath, sYearMonth, sProdId));
			if(!file.exists()) {
				poller.downloadProdClosingInfo(sYearMonth, sProdId);
			}
		}
		poller.downloadProdClosingInfo(lst.get(lst.size() - 1), sProdId);
	}
	
	private void checkAndDownloadDailyClosing(String sFilePath, List<String> lstDate, List<String> lstPOSTDate) throws IOException {
		for(int i = 0 ; i < lstDate.size() ; i++) {
			File file = new File(FileConstUtil.getDailyClosingFilePath(sFilePath, lstDate.get(i)));
			if(!file.exists()) {
				poller.downloadDailyClosingInfo(lstDate.get(i), lstPOSTDate.get(i));
			}
		}
	}
	
	public ArrayList<ProdClosingInfo> getProdClosingInfo(String sFrom, String sTo, String sProdId) throws ParseException, FileNotFoundException, IOException {
		List<String> lstYearMonth = TimeUtil.getYearMonthBetween(sdf.parse(sFrom), sdf.parse(sTo));
		ArrayList<ProdClosingInfo> lstProd = new ArrayList<ProdClosingInfo>();
		for(String sYearMonth : lstYearMonth) {
			File file = new File(FileConstUtil.getProdClosingFilePath(sFilePath, sYearMonth, sProdId));
			if(file.exists()) {
				lstProd.addAll(parser.readProdClosingInfo(sYearMonth, sProdId));
			}
		}
		return lstProd;
	}
	
	public ArrayList<DailyClosingInfo> getDailyClosingInfo(String sFrom, String sTo) throws ParseException, FileNotFoundException, IOException {
		List<String> lstDate = TimeUtil.getDateBetween(sdf.parse(sFrom), sdf.parse(sTo));
		ArrayList<DailyClosingInfo> lstInfo = new ArrayList<DailyClosingInfo>();
		for(String sDate : lstDate) {
			File file = new File(FileConstUtil.getDailyClosingFilePath(sFilePath, sDate));
			if(file.exists()) {
				DailyClosingInfo dailyClosingInfo = parser.readDailyClosingInfo(sDate);
				if(dailyClosingInfo != null) {
					lstInfo.add(dailyClosingInfo);
				}
			}
		}
		return lstInfo;
	}
}
