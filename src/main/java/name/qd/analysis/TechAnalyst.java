package name.qd.analysis;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.DataSourceFactory;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.tech.Analyzer;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.backTest.BackTesting;
import name.qd.analysis.tech.backTest.BackTestingFactory;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.tech.vo.VerifyResult;
import name.qd.analysis.tech.vo.VerifyResult.VerifyDetail;
import name.qd.analysis.utils.AnalystUtils;
import name.qd.analysis.utils.TimeUtil;

public class TechAnalyst {
	private Logger log = LoggerFactory.getLogger(TechAnalyst.class);
	private TechAnalyzerManager analyzerManager;
	private DataSource twseDataManager;
	
	private TechAnalyst() {
		analyzerManager = TechAnalyzerManager.getInstance();
		twseDataManager = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
//		new TechClient();
		
		List<AnalysisResult> lst = null;
		VerifyResult vf = null;
		try {
			Date from = TimeUtil.getDateTimeFormat().parse("20160101-00:00:00:000");
			Date to = TimeUtil.getDateTimeFormat().parse("20180403-00:00:00:000");
			String product = "0050";
			
			Analyzer analyzer = Analyzer.ABI;
//			outputResult(analyzer, product, from, to, 10, 40, 10);
			
			//
			analyzer = Analyzer.ABIAdvance;
//			outputResult(analyzer, product, from, to, 1, 600, 10);
			
			//
			analyzer = Analyzer.ABIDecline;
//			outputResult(analyzer, product, from, to, 1, 600, 10);
			//
			analyzer = Analyzer.ADL;
//			outputResult(analyzer, product, from, to);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void outputResult(Analyzer analyzer, String product, Date from, Date to, Object ... objs) {
		List<AnalysisResult> lst = getAnalysisResult(analyzer, product, from, to);
	}
	
	private List<AnalysisResult> getAnalysisResult(Analyzer analyzer, String product, Date from, Date to) {
		List<AnalysisResult> lst = null;
		try {
			lst = analyzerManager.getAnalysisResult(twseDataManager, analyzer, product, from, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(lst != null) {
			for(AnalysisResult result : lst) {
				System.out.println(result.getKeyString() + ":" + result.getValue());
			}
		}
		
		return lst;
	}
	
	public static void main(String[] args) {
		new TechAnalyst();
	}
}
