            package com.beans;
			import java.text.SimpleDateFormat;
			import java.util.*;
			import org.apache.hadoop.fs.FileStatus;

            import com.dao.impl.HdfsDaoImpl;

			
			//描述网盘文件信息的实体类
			public class DiskFileInfo {
				//被装饰对象
				private FileStatus f;
				
				//通过构造方法传入被装饰对象
				public DiskFileInfo(FileStatus f){
					this.f=f;
				}
				
				public String getModificationTime() {
					SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd hh:mm");
					return df.format(new Date(f.getModificationTime()));	
				}
				
					
				public boolean isDirectory() {
					return f.isDirectory();
				}
	
				public String getLen() {
					if(f.isDirectory()) {
						return "-";
					}
					
					/* 根据文件的字节数,计算出较友好的显示形式 Byte, K, M, G , T 等 */
					
					// 1024 * 1024 =1M =1048576
					// 1024 * 1024 *1024  =1G  =1073741824
					// 1024 * 1024 *1024*1024  =1T  =1099511627776 
					
					float size=f.getLen();
					
					if(size>=1099511627776L){
						return String.format("%.2f", size/1024/1024/1024/1024)+" P";
					}
					else if(size>=1073741824){
						return String.format("%.2f", size/1024/1024/1024)+" G";
					}
					
					else if(size>=1048576){
						return String.format("%.2f", size/1024/1024)+" M";
					}
					
					else if(size>1024)   {		
						return String.format("%.2f", size/1024)+" K";
					}
					
					else{
						return size+ " Byte"; 
					}	
				}
			
				private static Map<String,String> iconMap;
				
				//在静态代码块中,把图标和文件扩展名对应起来
				static {	
					iconMap=new HashMap<String, String>();
					iconMap.put("folder", "folder.png");  
					iconMap.put(".txt", "txtIcon.png");
					iconMap.put(".log", "txtIcon.png");
					iconMap.put(".gz", "zipIcon.png");
					iconMap.put(".rar", "zipIcon.png");
					iconMap.put(".jar", "zipIcon.png");
					iconMap.put(".zip", "zipIcon.png");
					iconMap.put(".7z", "zipIcon.png");
					iconMap.put(".exe", "exeIcon.png");
					iconMap.put(".avi", "aviIcon.png");
					iconMap.put(".wmv", "aviIcon.png");
					iconMap.put(".gif", "picIcon.png");
					iconMap.put(".bmp", "picIcon.png");
					iconMap.put(".jpg", "picIcon.png");
					iconMap.put(".jpeg", "picIcon.png");
					iconMap.put(".png", "picIcon.png");
					iconMap.put(".pdf", "pdfIcons.png");
					iconMap.put(".xls", "xlsIcon.png");
					iconMap.put(".xlsx", "xlsIcon.png");
					iconMap.put(".doc", "wordIcon.png");
					iconMap.put(".docx", "wordIcon.png");
					iconMap.put(".java", "sourceCodeIcon.png");
					iconMap.put(".data", "aviIcon.png");
					iconMap.put(".mp3", "soundIcon.png");	
				}
						
				public String getIcon() {   
					if(f.isDirectory()) {
						return iconMap.get("folder");
					}
					else {
						String fileName=f.getPath().getName();
						
						//如果文件有扩展名
						if(fileName.lastIndexOf(".")!=-1) {
							String ext=fileName.substring(fileName.lastIndexOf("."));
							String icon=iconMap.get(ext);
							
							return icon==null?"defaultIcon.png":icon;	
						}
						else {
							return "defaultIcon.png";
						}
					}
				}
			
				public String getPath() {
					return f.getPath().getName();
				}
				
				 public String getRealPath() {
						return f.getPath().toString().replace(HdfsDaoImpl.HDFS_PATH, "")+"";
					}
			
			}