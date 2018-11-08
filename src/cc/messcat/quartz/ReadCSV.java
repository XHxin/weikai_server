package cc.messcat.quartz;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther xiehuaxin
 * @create 2018-08-03 10:58
 * @todo
 */
public class ReadCSV {

    public static void main(String[] args)
    {
        File csv = new File("D:\\watch\\fund_bill_20160405\\20887019483452540156_20180802_业务明细.csv");  // CSV文件路径
        BufferedReader br = null;
        try
        {
            DataInputStream in = new DataInputStream(new FileInputStream(csv));
            br = new BufferedReader(new InputStreamReader(in,"gbk"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        try {
            List<String> allString = new ArrayList<>();
            boolean dataFormat = false;
            while ((line = br.readLine()) != null)  //读取到的内容给line变量
            {
                everyLine = line;
                System.out.println(everyLine);
                allString.add(everyLine);

                //确保表头信息一致，避免数据错乱
                if(allString.size() == 5 && everyLine.equals("支付宝交易号,商户订单号,业务类型,商品名称,创建时间,完成时间,门店编号,门店名称,操作员,终端号,对方账户,订单金额（元）,商家实收（元）,支付宝红包（元）,集分宝（元）,支付宝优惠（元）,商家优惠（元）,券核销金额（元）,券名称,商家红包消费金额（元）,卡消费金额（元）,退款批次号/请求号,服务费（元）,分润（元）,备注")) {
                    dataFormat = true;
                }
                if(dataFormat == true) {
                    String[] billColumn = everyLine.split(",");
                    AliPayBillRow billRow = new AliPayBillRow();
                    billRow.setMerchantOrderNum(billColumn[1].trim());
                    billRow.setDealType(billColumn[2].trim());
                    billRow.setOfficialReceipts(billColumn[12].trim());

                    //支付宝返回的账单只包含了“进账”和“出账”的账单，未支付的订单不会写进账单里
                    if(billRow.getDealType().equals("交易")) {

                    }
                }
            }
            System.out.println("csv表格中所有行数："+allString.size());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
