package com.dao.impl;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;

import com.beans.DiskFileInfo;
import com.dao.HdfsDao;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;


			
public class HdfsDaoImpl implements HdfsDao {
	public final static String HDFS_PATH="hdfs://hadoop100:9000/";
	private final static String USER_NAME="root";
	private static 	Configuration conf;
				
	//在静态代码块中初始 Configuration 对象
	static {
	conf=new Configuration();
					
	/*如果是ha集群,要进行下面的配置
	conf.set("dfs.nameservices","nameservice1");
	conf.set("dfs.ha.namenodes.nameservice1","nn1,nn2");
	conf.set("dfs.namenode.rpc-address.nameservice1.nn1","master:9000");
	conf.set("dfs.namenode.rpc-address.nameservice1.nn2","slave1:9000");
	conf.set("dfs.client.failover.proxy.provider.nameservice1","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
	*/
    	}
				
	/**
	 * 用户注册的时候,要在hdfs上同时创建一个目录,名称就是用账号
	 * 以后这个用户所有的文件都将在这个目录下
	 */
	public void createUserRoot(String userName) {
		 try {
			    FileSystem fs =FileSystem.get(URI.create(HDFS_PATH), conf, USER_NAME);
			    fs.mkdirs(new Path("/"+userName));
				fs.close();
			}
		catch(Exception ex) {
			throw new RuntimeException(ex);
				}
			}
	
	public DiskFileInfo[] getRootFileList(String userName) {
		try {
			FileSystem fs = FileSystem.get(URI.create(HDFS_PATH), conf);
			FileStatus[] fileList = fs.listStatus(new Path("/"+userName));
			DiskFileInfo[] diskFileList=new DiskFileInfo[fileList.length];
			for(int i=0;i<fileList.length;i++) {
				FileStatus f=fileList[i];
				diskFileList [i]=new DiskFileInfo(f);
			}
			
			return diskFileList;
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public DiskFileInfo[] getSubFileList(String parent) {
		try {
			FileSystem fs = FileSystem.get(URI.create(HDFS_PATH), conf);
			FileStatus[] fileList = fs.listStatus(new Path("/"+parent));
			DiskFileInfo[] diskFileList=new DiskFileInfo[fileList.length];
			for(int i=0;i<fileList.length;i++) {
				FileStatus f=fileList[i];
				diskFileList [i]=new DiskFileInfo(f);
			}
			
			return diskFileList;
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}	
		
	public static void main(String[] args) {
		HdfsDaoImpl imp=new HdfsDaoImpl();
		DiskFileInfo [] list=imp.getSubFileList("/root/java");		
		System.out.println("ok");
		
		for(DiskFileInfo f:list) {
			System.out.println(f.getPath());
		        }
            }	
	
	public void downLoadFileAsStream(String fileName, ServletOutputStream out) {
			try {
				FileSystem fs = FileSystem.get(URI.create(HDFS_PATH),conf);
		        Path path = new Path("/"+fileName);
		        FSDataInputStream fsInput = fs.open(path);
		        IOUtils.copyBytes(fsInput, out, 4096, false);
		        out.flush();
		        fsInput.close();
			}
			catch(Exception ex) {
				throw new RuntimeException(ex);
			}	
		}

	@Override
	public void uploadFile(String parent,String localPath) {
		try{
			FileSystem fs = FileSystem.get(URI.create(HDFS_PATH),conf,USER_NAME);		
			fs.moveFromLocalFile(new Path(localPath), new Path("/"+parent));
			fs.close();
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	@Override
	public long getUserDiskSize(String userRoot) {
		try {
			FileSystem fs = FileSystem.get(URI.create(HDFS_PATH), conf);
			long size=fs.getContentSummary(new Path("/"+userRoot)).getLength();
			fs.close();
			return size;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void deleteFile(String fileName) {
		try{
			FileSystem fs = FileSystem.get(URI.create(HDFS_PATH),conf,USER_NAME);	
			fs.delete(new Path("/"+fileName), true);  //若不用true ,文件夹非空,删不掉,true表示递归
			fs.close();
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}	

	@Override
	public boolean createFolder(String parent, String folderName) {
		try{
			FileSystem fs = FileSystem.get(URI.create(HDFS_PATH),conf,USER_NAME);	
			boolean result= fs.mkdirs(new Path("/"+parent+"/"+folderName));
			fs.close();
			return result;
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
		
	}

	@Override
	//根据文件的名查询所有文件,不区分大小写
		public List<DiskFileInfo> getFileListByName(String userName, String fileName) {
			fileName=fileName.toLowerCase();
			List<DiskFileInfo> fileList=new ArrayList<DiskFileInfo>();
			try {
				FileSystem fs = FileSystem.get(URI.create(HDFS_PATH), conf);
			
				//这个方法后面的参数true 表示要用递归
				RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path(HDFS_PATH+userName), true);
			
				while(files.hasNext()) {
					LocatedFileStatus file = files.next();
					if(file.getPath().getName().toLowerCase().contains(fileName)) {
						DiskFileInfo info=new DiskFileInfo(file);
						fileList.add(info);
					}
				}
				
				return fileList;
			}
			catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	private static Map<String,String []> typeToFileExtMap ;
	static {
		typeToFileExtMap=new HashMap<String,String []>();
		typeToFileExtMap.put("picture",new String[]{".jpeg",".bmp",".gif",".png",".tiff",".psd",".eps",".raw",".pdf",".png",".pxr",".mac",".jpg",".tga",".img",".pcd"});
		typeToFileExtMap.put("txt",new String[]{".txt",".doc",".docx",".wps",".xls",".xlsx",".java",".pdf",".c",".h",".cpp",".xml",".log"});
		typeToFileExtMap.put("avi",new String[]{".wmv",".avi",".wma",".rmvb",".rm",".flash",".mp4",".mid",".3gp",".mpg", ".mp2v",".mpv2",".m1v",".dat",".dsa",".mkv"});
		typeToFileExtMap.put("sound",new String[]{".mp3",".wma",".avi",".rm",".rmvb",".flv",".mpg",".mov",".mkv"});
		typeToFileExtMap.put("gz",new String[]{".gz",".zip",".rar",".z",".bz",".bz2",".7z",".mov",".mkv"});
		typeToFileExtMap.put("torrent", new String[]{".torrent", ".bt"});
	}
	public List<DiskFileInfo> getFileListByType(String userName, String fileType) {
		List<DiskFileInfo> fileList=new ArrayList<>();
		
		try {
			String userPath=HDFS_PATH+userName;
			FileSystem fs=FileSystem.get(URI.create(userPath),conf,USER_NAME);
			
			//递归遍历
			RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path(userPath), true);
			while(files.hasNext()) {
				LocatedFileStatus file = files.next();
			
				//过滤出想要的类型的文件
				String [] fileExtList =typeToFileExtMap.get(fileType);
				
				for(String ext:fileExtList) {
					if(file.getPath().getName().toLowerCase().endsWith(ext)) {
						DiskFileInfo info=new DiskFileInfo(file);
						fileList.add(info);
						break;
					}
				}
			}
		}
		
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		
		return fileList;
	}
}	