package com.findex.demo.indexData.index.service;

import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexData.index.repository.IndexDataRepository;
import com.opencsv.CSVWriter;
import java.io.OutputStreamWriter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CSVExportIndexDataService {

  private final IndexDataRepository indexDataRepository;

  public byte[] downloadCsv() throws Exception {

    try{
      List<IndexData> dataList = indexDataRepository.findAll();

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream));

      // CSV Header
      String[] header = {"id", "infoId", "baseDate", "sourceType", "openPrice", "closePrice",
      "highPrice", "lowPrice", "versus","fluctuationRate","trandingQuantity","trandingPrice",
      "marketTotalAmount"};
      csvWriter.writeNext(header);

      // 사용자 데이터로 CSV 작성
      for (IndexData data : dataList) {
        String[] datas = {
            data.getId().toString(),
            data.getIndexInfo().getId().toString(),
            data.getBaseDate().toString(),
            data.getSourceType().toString(),
            data.getOpenPrice().toString(),
            data.getClosePrice().toString(),
            data.getHighPrice().toString(),
            data.getLowPrice().toString(),
            data.getVersus().toString(),
            data.getFluctuationRate().toString(),
            data.getTradingQuantity().toString(),
            data.getTradingPrice().toString(),
            data.getMarketTotalAmount().toString(),
        };
        csvWriter.writeNext(datas);
      }


     // csvWriter.close();

      // CSV ->  byte[]
      return outputStream.toByteArray();
    } catch (Exception e) {
       throw  new CustomException(ErrorCode.INTERNAL_SERVER_ERROR , "서버 처리에 처리 하기 어려울 것 같습니다.");
    }
  }
}

