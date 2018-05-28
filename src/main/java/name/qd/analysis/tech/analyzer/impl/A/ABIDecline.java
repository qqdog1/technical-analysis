package name.qd.analysis.tech.analyzer.impl.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.DailyClosingInfo;
import name.qd.analysis.tech.analyzer.TechAnalyzer;
import name.qd.analysis.tech.vo.AnalysisResult;

public class ABIDecline implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ABIDecline.class);
	
	@Override
	public String getCacheName(String product) {
		return ABIDecline.class.getSimpleName();
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataSource, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lstResult = new ArrayList<>();
		try {
			List<DailyClosingInfo> lstDaily = dataSource.getDailyClosingInfo(from, to);
			for(DailyClosingInfo info : lstDaily) {
				AnalysisResult result = new AnalysisResult();
				result.setDate(info.getDate());
				int value = info.getDecline()-info.getAdvance();
				int total = info.getDecline()+info.getAdvance()+info.getUnchanged();
				result.setValue(value);
				result.setValue(total);
				lstResult.add(result);
			}
		} catch (Exception e) {
			log.error("ABI analyze failed.", e);
			throw e;
		}
		return lstResult;
	}
	
	@Override
	public List<AnalysisResult> customResult(DataSource dataSource, String product, Date from, Date to, String ... inputs) throws Exception {
		return null;
	}
	
	@Override
	public List<String> getCustomDescreption() {
		return null;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return AnalyzerType.MARKET;
	}
}
