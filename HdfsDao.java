package com.dao;
import java.util.List;

import javax.servlet.ServletOutputStream;

import com.beans.DiskFileInfo;

public interface HdfsDao {
	void createUserRoot(String userName);
	DiskFileInfo[] getRootFileList(String userName);
	DiskFileInfo[] getSubFileList(String parent);
    void downLoadFileAsStream(String fileName, ServletOutputStream outputStream);
    void uploadFile(String parent, String localPath);

	long getUserDiskSize(String userName);
	void deleteFile(String fileName);
	boolean createFolder(String parent, String folderName);
	List<DiskFileInfo> getFileListByName(String userName, String fileName);
	List<DiskFileInfo> getFileListByType(String userName, String fileType);
}


