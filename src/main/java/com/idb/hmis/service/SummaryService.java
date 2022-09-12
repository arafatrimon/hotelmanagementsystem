package com.idb.hmis.service;

import java.util.Map;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author Yasir Araphat
 */
public interface SummaryService {

    Map<String, Object> getBranchSummary(Long branchId);

    JasperPrint getReport(String reportType, Integer year, Long hostelId, Long branchId) throws Exception;

}
