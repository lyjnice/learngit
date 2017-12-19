import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

public class poitest {
   public static void main(String[] args) {
 
		String realname = new String("研修活动计划汇总.xls".getBytes("GBK"),"ISO-8859-1");
//		filetype = "application/msexcel";
		//生成XLS
		//创建Excel
		this.getResponse().setHeader("Content-Disposition", "attachment; filename=" + realname);
		this.getResponse().setContentType("application/vnd.ms-excel");
	    OutputStream os = this.getResponse().getOutputStream();
	    WritableWorkbook workbook = Workbook.createWorkbook(os);
	    //创建sheet
	    String str = "研修活动计划汇总";
	    int t = 0;
	    String s = "研修活动计划汇总";
	    if(entity.getSchoolyear()!=null){
	    	t = entity.getSchoolyear()+1;
			s = entity.getSchoolyear()+"-"+t+"学年研修活动计划汇总";
		}
	    WritableSheet sheet1 = workbook.createSheet(s, 0);
	    //样式
	    WritableFont wf = new WritableFont(jxl.write.WritableFont.ARIAL, 14,jxl.write.WritableFont.BOLD, false);
	    WritableCellFormat wcfF = new WritableCellFormat(wf);
	    wcfF.setVerticalAlignment(VerticalAlignment.CENTRE);
//	    sheet1.addCell(new Label(0,0,"学校编号",wcfF));
	    wcfF.setWrap(true);
	    int i =0;
   	sheet1.addCell(new Label(0,i,"序号",wcfF));
   	sheet1.addCell(new Label(1,i,"活动日期",wcfF));
   	sheet1.addCell(new Label(2,i,"活动主题",wcfF));
   	sheet1.addCell(new Label(3,i,"主持人",wcfF));
   	sheet1.addCell(new Label(4,i,"活动类型",wcfF));
   	sheet1.addCell(new Label(5,i,"学期",wcfF));
   	sheet1.addCell(new Label(6,i,"发布人",wcfF));
   	sheet1.addCell(new Label(7,i,"主讲人",wcfF));
   	sheet1.addCell(new Label(8,i,"职称",wcfF));
   	sheet1.addCell(new Label(9,i,"单位",wcfF));
	    if(courseactPage!=null && courseactPage.getResult()!=null && courseactPage.getResult().size()>0){
	    	i =i +1;
	    	String term = "";
	    	for(CourseActive cat : courseactPage.getResult()){
	    		
	    		Course c = null;
	    		try{
	    			c = courseService.findById(cat.getCourseid());
	    		}catch(Exception ex){
	    			c = null;
	    		}
		    	sheet1.addCell(new Label(0,i,String.valueOf(i)));
		    	if(cat.getDatetime()!=null){
		    		sheet1.addCell(new Label(1,i,DateUtils.fotmatDate4(cat.getDatetime())));
		    	}else{
		    		sheet1.addCell(new Label(1,i," "));
		    	}
		    	sheet1.addCell(new Label(2,i,cat.getName()));
		    	sheet1.addCell(new Label(3,i,cat.getHostname()));
		    	if(cat.getActivetype()!=null){
	    			if(c!=null && c.getCoursetype()!=null && c.getCoursetype().intValue()==11){
	    				sheet1.addCell(new Label(4,i,"学科活动"));
	    			}else if(c!=null && c.getCoursetype()!=null && c.getCoursetype().intValue()==15){
	    				sheet1.addCell(new Label(4,i,"跨学科活动"));
	    			}else if(cat.getActivetype().equals(0)){
	    				sheet1.addCell(new Label(4,i,"年级活动"));
	    			}else{
	    				sheet1.addCell(new Label(4,i,""));
	    			}
//		    		if(cat.getActivetype().equals(0)){
//		    			sheet1.addCell(new Label(6,i,"年级活动"));
//		    		}else if(cat.getActivetype().equals(1)){
//		    			sheet1.addCell(new Label(6,i,"学科活动"));
//		    		}else if(cat.getActivetype().equals(2)){
//		    			sheet1.addCell(new Label(6,i,"综合素养活动"));
//		    		}
		    	}else{
		    		sheet1.addCell(new Label(4,i," "));
		    	}
//		    	if(cat.getWayname()!=null && !cat.getWayname().equals("")){
//		    		sheet1.addCell(new Label(6,i,cat.getWayname()));
//		    	}else{
//		    		sheet1.addCell(new Label(6,i," "));
//		    	}
		    	if(cat.getCourse()!=null && cat.getCourse().getSchoolyear()!=null){
		    		t = cat.getCourse().getSchoolyear()+1;
		    		term = cat.getCourse().getSchoolyear()+"-"+t+"学年";
		    		if(cat.getCourse().getTerm()!=null){
		    			term = term + "("+cat.getCourse().getTermname()+")";
		    		}
		    	}
		    	sheet1.addCell(new Label(5,i,term));
		    	if(cat.getUserbase()!=null){
		    		sheet1.addCell(new Label(6,i,cat.getUserbase().getTruename()));
		    	}else{
		    		sheet1.addCell(new Label(6,i,""));
		    	}
		    	String[] speaker = cat.getSpeaker().split(",");
		        String[] technicalpost = new String[speaker.length];
		        String[] technicalpost1 = cat.getTechnicalpost().split(",");
		        System.arraycopy(technicalpost1, 0, technicalpost, 0,technicalpost1.length);
		        String[] technicalpostname =new String[speaker.length];
		        String[] technicalpostname1 = cat.getTechnicalpostname().split(",");;
		        System.arraycopy(technicalpostname1, 0, technicalpostname, 0, technicalpostname1.length);
		        String[] othertechnicalpost = new String[speaker.length];
		        String[] othertechnicalpost1 =  cat.getOthertechnicalpost().split(",");
		        System.arraycopy(othertechnicalpost1, 0, othertechnicalpost, 0, othertechnicalpost1.length);
		        String[] unit =  new String[speaker.length];
		        String[] unit1 = cat.getUnit().split(",");
		        System.arraycopy(unit1, 0, unit, 0, unit1.length);
		        for (int j = 0; j < speaker.length; j++) {
		        	String postname = "";
		        	if("null".equals(othertechnicalpost[j]) ||(othertechnicalpost[j] == null)){
		        		postname =technicalpostname[j];
		        	}else{
		        		postname =technicalpostname[j] +"("+othertechnicalpost[j]+")";
		        	}
		            sheet1.addCell(new Label((index++),i,speaker[j]));
		            sheet1.addCell(new Label((index++),i,postname));
		            sheet1.addCell(new Label((index++),i,unit[j]));
				}
	    	}
	    }
	    if(coursebasedactPage!=null && coursebasedactPage.getResult()!=null && coursebasedactPage.getResult().size()>0){
	    	if(i == 0) i =i +1;
	    	String term = "";
	    	for(CourseActive cat : coursebasedactPage.getResult()){
			   if((stime != null &&!"".equals(stime) && !DateUtils.fotmatDate4(cat.getDatetime()).equals(stime))) continue;
	    		CourseBased c = null;
	    		try{
	    			c = courseBasedService.findById(cat.getCourseid());
	    		}catch(Exception ex){
	    			c = null;
	    		}
	    		sheet1.addCell(new Label(0,i,String.valueOf(i)));
		    	if(cat.getDatetime()!=null){
		    		sheet1.addCell(new Label(1,i,DateUtils.fotmatDate4(cat.getDatetime())));
		    	}else{
		    		sheet1.addCell(new Label(1,i," "));
		    	}
		    	sheet1.addCell(new Label(2,i,cat.getName()));
		    	sheet1.addCell(new Label(3,i,cat.getHostname()));
		    	if(cat.getActivetype()!=null){
	    			if(c!=null && c.getCoursetype()!=null && c.getCoursetype().intValue()==11){
	    				sheet1.addCell(new Label(4,i,"学科活动"));
	    			}else if(c!=null && c.getCoursetype()!=null && c.getCoursetype().intValue()==15){
	    				sheet1.addCell(new Label(4,i,"跨学科活动"));
	    			}else if(cat.getActivetype().equals(0)){
	    				sheet1.addCell(new Label(4,i,"年级活动"));
	    			}else{
	    				sheet1.addCell(new Label(4,i,""));
	    			}
//		    		if(cat.getActivetype().equals(0)){
//		    			sheet1.addCell(new Label(6,i,"年级活动"));
//		    		}else if(cat.getActivetype().equals(1)){
//		    			sheet1.addCell(new Label(6,i,"学科活动"));
//		    		}else if(cat.getActivetype().equals(2)){
//		    			sheet1.addCell(new Label(6,i,"综合素养活动"));
//		    		}
		    	}else{
		    		sheet1.addCell(new Label(4,i," "));
		    	}
//		    	if(cat.getWayname()!=null && !cat.getWayname().equals("")){
//		    		sheet1.addCell(new Label(6,i,cat.getWayname()));
//		    	}else{
//		    		sheet1.addCell(new Label(6,i," "));
//		    	}
		    	if(cat.getCoursebased()!=null && cat.getCoursebased().getSchoolyear()!=null){
		    		t = cat.getCoursebased().getSchoolyear()+1;
		    		term = cat.getCoursebased().getSchoolyear()+"-"+t+"学年";
		    		if(cat.getCoursebased().getTerm()!=null){
		    			term = term + "("+cat.getCoursebased().getTermname()+")";
		    		}
		    	}
		    	sheet1.addCell(new Label(5,i,term));
		    	if(cat.getCoursebased()!=null){
		    		sheet1.addCell(new Label(6,i,cat.getCoursebased().getUserbase().getTruename()));
		    	}else{
		    		sheet1.addCell(new Label(6,i,""));
		    	}
		    	String[] speaker = cat.getSpeaker().split(",");
		        String[] technicalpost = new String[speaker.length];
		        String[] technicalpost1 = cat.getTechnicalpost().split(",");
		        System.arraycopy(technicalpost1, 0, technicalpost, 0,technicalpost1.length);
		        String[] technicalpostname =new String[speaker.length];
		        String[] technicalpostname1 = cat.getTechnicalpostname().split(",");;
		        System.arraycopy(technicalpostname1, 0, technicalpostname, 0, technicalpostname1.length);
		        String[] othertechnicalpost = new String[speaker.length];
		        String[] othertechnicalpost1 =  cat.getOthertechnicalpost().split(",");
		        System.arraycopy(othertechnicalpost1, 0, othertechnicalpost, 0, othertechnicalpost1.length);
		        String[] unit =  new String[speaker.length];
		        String[] unit1 = cat.getUnit().split(",");
		        System.arraycopy(unit1, 0, unit, 0, unit1.length);
		        int index = 7;
		        for (int j = 0; j < speaker.length; j++) {
		        	String postname = "";
		        	if("null".equals(othertechnicalpost[j]) ||(othertechnicalpost[j] == null)){
		        		postname =technicalpostname[j];
		        	}else{
		        		postname =technicalpostname[j] +"("+othertechnicalpost[j]+")";
		        	}
		            sheet1.addCell(new Label((index++),i,speaker[j]));
		            sheet1.addCell(new Label((index++),i,postname));
		            sheet1.addCell(new Label((index++),i,unit[j]));
				}
		    	i = i + 1;
	    	}
	    }
	    workbook.write();
		workbook.close();
		os.flush();
		os.close();
		return null;
	
}
}
