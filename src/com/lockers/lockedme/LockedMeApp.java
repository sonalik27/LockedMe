package com.lockers.lockedme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LockedMeApp {	
	
	private static final String ERROR_MESSAGE_INVALID_INPUT = "Invalid Input, please try again..";
	private static final String MESSAGE_FILE_ADD_SUCCESS = "File added succesfully!!";
	private static final String LOCKER_ROOT_FOLDER = "LockedMeData";
	
	LockedMeApp(){
		try {
			File rootFolder = new File(LOCKER_ROOT_FOLDER);
			boolean rootDirExist = rootFolder.exists();
			
			if(!rootDirExist) {			
				rootDirExist = rootFolder.mkdir();
			}		
		}catch(SecurityException se) {
			System.out.println("File access permission denied. LockedMe application cannot be run on your system and needs to close.");
			System.exit(0);
		}
	}
		
	public void start() {
		showHeader();
		showMainScreen();
	}
	
	private void showMainScreen(){
		showAndProcessMainScreen();
	}
	
	private void showHeader() {
		System.out.println("--------------------------------------------------------------------------------------------------");
		System.out.println("----------------------------------- Welcome to LockedMe -----------------------------------------");
		System.out.println("----------------------------------- Developer: Sonali Parab --------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------------------");
	}
	
	private void showMainScreenInputMessage() {
		System.out.println("--------------------------------------------------------------------------------------------------");
		System.out.println("------------------------------------------- Main Screen   ----------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------------------");	
		System.out.println("\nPlease select following options for respective actions to take");
		System.out.println(" 1 - List files (Files are listed in ascending order by names)");
		System.out.println(" 2 - Open menu of File Operations");
		System.out.println(" 3 - Exit");
		System.out.println(" Enter you option here : ");
			
	}	
	
	private void showOptionScreenInputMessage() {
		System.out.println("--------------------------------------------------------------------------------------------------");
		System.out.println("------------------------------------------- Options Screen ---------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------------------");	
		System.out.println("\nPlease select following options for respective actions to take");
		System.out.println(" 1 - Add File");
		System.out.println(" 2 - Delete File");
		System.out.println(" 3 - Search File");
		System.out.println(" 4 - Back to Main Screen");
		System.out.println("\n");	
		System.out.println(" Enter you option here : ");	
	}	
	
	private void showAndProcessMainScreen() {
		boolean exit = false;
		//Show UI
		showMainScreenInputMessage();
		
		while(!exit) {
			//Take Input and process
			try {
				Scanner sc = new Scanner(System.in);  
				int option= 0;
				
				try{
					option = Integer.parseInt(sc.nextLine());     //reads string  
				}catch(NumberFormatException nfe) {} 
			
				switch(option) {
					case 1 : listFiles(); break;
					case 2 : showAndProcessOptionScreen();break;
					case 3 : exit = true; break;
					default : System.out.println(ERROR_MESSAGE_INVALID_INPUT);		
							  showMainScreenInputMessage();
				}
			}catch(InputMismatchException  e) {
				System.out.println(ERROR_MESSAGE_INVALID_INPUT);
				showMainScreenInputMessage();
			}catch(LockedMeException dle) {
				System.out.println("Error:" + dle.getMessage());
				showMainScreenInputMessage();
			}catch(Exception e) {
				System.out.println("\n An error has occurred and the application needs to close.");
				System.exit(0);
			}
		}
		
	}
	
	private void showAndProcessOptionScreen() {
		boolean exitOptionScreen = false;
		showOptionScreenInputMessage();
		
		while(!exitOptionScreen) {
			//Take Input and process
			try {				

				Scanner sc = new Scanner(System.in);  
				int option= 0;
				
				try{
					option = Integer.parseInt(sc.nextLine()); 
				}catch(NumberFormatException nfe) {}
			
				switch(option) {
					case 1 : System.out.println("Please enter PATH of File to be Added: ");
							 String path = sc.nextLine();
							 addFile(path); break;
					case 2 : System.out.println("Please enter name of File to be Deleted: ");
					 		 deleteFile(sc.nextLine()); break;
					case 3 : System.out.println("Please enter name of File to be Searched: ");
							 searchFile(sc.nextLine()); break;
					case 4 : showAndProcessMainScreen(); 
							 exitOptionScreen = true; break;
					default : System.out.println(ERROR_MESSAGE_INVALID_INPUT);
							  showOptionScreenInputMessage();
				}
			}catch(InputMismatchException  ime) {
				System.out.println("Error:" + ERROR_MESSAGE_INVALID_INPUT);
				showAndProcessOptionScreen();
			}catch(LockedMeException dle) {
				System.out.println("Error:" + dle.getMessage());
				showAndProcessOptionScreen();
			}catch(Exception e) {
				System.out.println("\nAn error has occurred and the application needs to close.");
				System.exit(0);
			}
		}
		
	}
	

	private void addFile(String sourceFilePath) throws LockedMeException{
		File rootFolder = new File(LOCKER_ROOT_FOLDER);
		boolean rootDirExist = rootFolder.exists();
		
		if(!rootDirExist) {			
			rootDirExist = rootFolder.mkdir();
		}
		
		if(rootDirExist) {
			//Add the file to root directory
			if(sourceFilePath==null || sourceFilePath.isEmpty()) {
				throw new LockedMeException("Source File path cannot be empty.");
			}else {
			    File srcFile = new File(sourceFilePath);

			    if(!srcFile.exists()) {
			    	throw new LockedMeException("Invalid path, please check again : " + srcFile);
			    }else {
			    	 File destFile = new File(rootFolder, srcFile.getName());
			    	 if(destFile.exists()) {
			    		 throw new LockedMeException("LockedMe file '" + srcFile.getName() + "' already exists");
			    	 }else {
			    		 try {
			    			 if(destFile.createNewFile()) {
			    				 copyFileData(srcFile, destFile);
			    				 System.out.println(MESSAGE_FILE_ADD_SUCCESS);
			    			 }else {
			    				 throw new LockedMeException("Creation of new file failed");
			    			 }
			    		 }catch(IOException ioe) {
			    			 throw new LockedMeException("Creation of new file failed");
			    		 }
			    		 
			    	 }
			    }					
				
			}
		}	           
				
	}


	private void copyFileData(File srcFile, File destFile) throws LockedMeException{
		FileInputStream fin = null;
		FileOutputStream fout = null;
		
		 try {
			  fin=new FileInputStream(srcFile);
			  fout=new FileOutputStream(destFile,true);

			  int b;
			  while((b=fin.read()) != -1) { 
				  fout.write(b); 
			  }
			
		} catch (Exception e) {
			destFile.delete();
			throw new LockedMeException("Error occurred while writing the file");
		}finally {
			if(fin!=null) {
				try {
					fin.close();
				} catch (IOException ioe) {
					System.out.println("Error closing the input stream: " + ioe.getMessage() );
				}
			}
			if(fout!=null) {
				try {
					fout.close();
				} catch (IOException ioe) {
					System.out.println("Error closing the output stream: " + ioe.getMessage() );
				}
			}
		}
		
	}

	private void searchFile(String fileName) throws LockedMeException{
		if(fileName==null || fileName.isEmpty()) {
			throw new LockedMeException("fileName cannot be empty.");
		}else {
			File rootFolder = new File(LOCKER_ROOT_FOLDER);		
		    File file = new File(rootFolder, fileName);
		    if(!file.exists()) {		    	
		    	 System.out.println("File does not exists");
		    }else {
		       printFileDetails(file);	    		    
		    }
		}
		
	}

	private void deleteFile(String fileName) throws LockedMeException{
		if(fileName==null || fileName.isEmpty()) {
			throw new LockedMeException("fileName cannot be empty.");
		}else {
			File rootFolder = new File(LOCKER_ROOT_FOLDER);		
		    File file = new File(rootFolder, fileName);
		    if(!file.exists()) {		    	
		    	 throw new LockedMeException("File does not exists");
		    }else {
		       if(file.delete()) {
		    	   System.out.println("File deleted succesfully");
		       }else {
		    	   throw new LockedMeException("Unable to delete the file");
		       }	    		    
		    }
		}
		
	}
    
	private void listFiles() throws LockedMeException{	
		int count = 0;
		File rootFolder = new File(LOCKER_ROOT_FOLDER);
		if(rootFolder.exists()) {
			try {
				File[] fileList = rootFolder.listFiles();
				count = fileList.length;
				System.out.println("  " + count + " file(s) found \n");
				if(fileList!=null && fileList.length>0) {
					Arrays.sort(fileList);
					for(File file : fileList) {
						printFileDetails(file); 
					}
				}
			}catch(SecurityException se) {
				throw new LockedMeException("Unable to list files.Access Denied");
			}
		}		
		
	}
	
	private void printFileDetails(File file) {
		if(file!=null && file.exists()){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			System.out.println(file.getName() + "\t\t" + sdf.format(file.lastModified()));
		}
	}
	

}
