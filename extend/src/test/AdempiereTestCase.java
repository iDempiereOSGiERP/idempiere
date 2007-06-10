/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 Adempiere, Inc. All Rights Reserved.               *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JFileChooser;

import org.compiere.util.CLogMgt;
import org.compiere.util.Ini;
import org.compiere.util.Trx;

import junit.framework.TestCase;

/**
 * ADempiere Base Test Case
 * @author Teo Sarca
 */
public class AdempiereTestCase extends TestCase {

	// Test: General
	protected Properties testProperties = null;

	// Test Context
	private Properties m_Ctx = null;

	// Filename
	public final String fileName_Key = "AdempiereProperties";
	private String fileName_DefaultValue = "J:/Trifon-CD-0.3/workspace/adempiere-trunk/adempiere/Adempiere/Adempiere.properties";
	private String fileName_Value = "";

	// IsClient
	public final String isClient_Key = "isClient";
	private String isClient_DefaultValue = "Y";
	private boolean isClient_Value = true;

	// AD_User
	public final String AD_User_ID_Key = "AD_User_ID";
	private String AD_User_ID_DefaultValue = "0";
	private int AD_User_ID_Value = 0;

	// AD_Client
	public final String AD_Client_ID_Key = "AD_Client_ID";
	private String AD_Client_ID_DefaultValue = "11";
	private int AD_Client_ID_Value = 11;

	// LogLevel:
	public final String LogLevel_Key = "LogLevel";
	private String LogLevel_DefaultValue = Level.FINEST.toString();
	private Level LogLevel_Value = Level.FINEST;

	// Trx name
	private String trxName = "test";

	public Properties getCtx() {
		return m_Ctx;
	}

	public String getTrxName() {
		return trxName;
	}
	
	public int getAD_Client_ID() {
		return AD_Client_ID_Value;
	}

	public int getAD_User_ID() {
		return AD_User_ID_Value;
	}
	
	public boolean isClient() {
		return isClient_Value;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Load properties
		testProperties = new Properties();
		File file = new File("test.properties");
		if (!file.isFile()) {
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
			}
			else {
				file = null;
			}
		}
		testProperties.load(new FileInputStream(file));

		//
		fileName_Value = testProperties.getProperty(fileName_Key, fileName_DefaultValue);
		isClient_Value = "Y".equals( testProperties.getProperty(isClient_Key, isClient_DefaultValue) );
		AD_User_ID_Value = Integer.parseInt(testProperties.getProperty(AD_User_ID_Key, AD_User_ID_DefaultValue) );
		AD_Client_ID_Value = Integer.parseInt(testProperties.getProperty(AD_Client_ID_Key, AD_Client_ID_DefaultValue) );
		try {
			LogLevel_Value = Level.parse(testProperties.getProperty(LogLevel_Key, LogLevel_DefaultValue));
		} catch (Exception e) {
		}

		m_Ctx = new Properties();
		m_Ctx.setProperty("#AD_User_ID", new Integer(AD_User_ID_Value).toString());
		m_Ctx.setProperty("#AD_Client_ID", new Integer(AD_Client_ID_Value).toString());

		if (fileName_Value.length() < 1) {
			assertEquals("Please specify path to Adempiere.properties file!", true, false);
		}

		System.setProperty("PropertyFile", fileName_Value);
		Ini.setClient (isClient_Value);
		org.compiere.Adempiere.startup(isClient_Value);

		CLogMgt.setLevel(LogLevel_Value);
	}


	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		// Rollback the transaction, if any
		Trx trx = null;
		if (trxName != null)
			trx = Trx.get(trxName, false);
		if (trx != null && trx.isActive()) { 
			trx.rollback();
			trx.close();
		}
		trx = null;

		testProperties = null;
		m_Ctx = null;
	}
}
