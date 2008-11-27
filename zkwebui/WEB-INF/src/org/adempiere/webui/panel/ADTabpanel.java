/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.component.Bandbox;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.Datebox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridPanel;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.SimpleTreeModel;
import org.adempiere.webui.editor.IZoomableEditor;
import org.adempiere.webui.editor.WButtonEditor;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WEditorPopupMenu;
import org.adempiere.webui.editor.WebEditorFactory;
import org.adempiere.webui.event.ContextMenuListener;
import org.adempiere.webui.util.GridTabDataBinder;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.DataStatusEvent;
import org.compiere.model.DataStatusListener;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridWindow;
import org.compiere.model.MLookup;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.model.X_AD_FieldGroup;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Evaluatee;
import org.zkoss.zk.au.out.AuFocus;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Div;
import org.zkoss.zul.Group;
import org.zkoss.zul.Separator;
import org.zkoss.zul.SimpleTreeNode;
import org.zkoss.zul.Space;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

/**
 * 
 * This class is based on org.compiere.grid.GridController written by Jorg Janke.
 * Changes have been brought for UI compatibility.
 * 
 * @author Jorg Janke
 * 
 * @author <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date Feb 25, 2007
 * @version $Revision: 0.10 $
 *
 * @author Low Heng Sin 
 */
public class ADTabpanel extends Div implements Evaluatee, EventListener, 
DataStatusListener, IADTabpanel
{
    
	private static final long serialVersionUID = 212250368715189455L;

	private static final CLogger logger;
    
    static
    {
        logger = CLogger.getCLogger(ADTabpanel.class);
    }

    private GridTab           gridTab;

    @SuppressWarnings("unused")
	private GridWindow        gridWindow;

    private AbstractADWindowPanel      windowPanel;

    private int               windowNo;

    private Grid              grid;
    
    private ArrayList<WEditor> editors = new ArrayList<WEditor>();
    
    private ArrayList<String> editorIds = new ArrayList<String>();
       
    private boolean			  uiCreated = false;
    
    private GridPanel		  listPanel;
    
    private Map<String, List<org.zkoss.zul.Row>> fieldGroupContents = new HashMap<String, List<org.zkoss.zul.Row>>();
    
    private Map<String, List<org.zkoss.zul.Row>> fieldGroupHeaders = new HashMap<String, List<org.zkoss.zul.Row>>();

	private ArrayList<org.zkoss.zul.Row> rowList;
	
	private Component formComponent = null;
	
	private Tree tree = null;

	private GridTabDataBinder dataBinder;

	private Map<Integer, Group> includedTab = new HashMap<Integer, Group>();
	
	private List<EmbeddedPanel> includedPanel = new ArrayList<EmbeddedPanel>();
	
	private boolean active = false;

	public ADTabpanel() 
	{
        init();
    }

    private void init()
    {
        initComponents();
    }

    private void initComponents()
    {
    	LayoutUtils.addSclass("adtab-content", this);    	    	    		
    	
        grid = new Grid();
        //have problem moving the following out as css class
        grid.setWidth("99%");
        grid.setHeight("100%");
        grid.setVflex(true);
        grid.setStyle("margin:0; padding:0; position: absolute");
        grid.makeNoStrip();
                
        listPanel = new GridPanel();
        listPanel.getListbox().addEventListener(Events.ON_DOUBLE_CLICK, this);
                
    }

    public void init(AbstractADWindowPanel winPanel, int windowNo, GridTab gridTab,
            GridWindow gridWindow)
    {
        this.windowNo = windowNo;
        this.gridWindow = gridWindow;
        this.gridTab = gridTab;
        this.windowPanel = winPanel;
        gridTab.addDataStatusListener(this);
        this.dataBinder = new GridTabDataBinder(gridTab);
        
        this.getChildren().clear();
                
        int AD_Tree_ID = 0;
		if (gridTab.isTreeTab())
			AD_Tree_ID = MTree.getDefaultAD_Tree_ID (
				Env.getAD_Client_ID(Env.getCtx()), gridTab.getKeyColumnName());
		if (gridTab.isTreeTab() && AD_Tree_ID != 0)
		{
			Borderlayout layout = new Borderlayout();
			layout.setParent(this);
			layout.setStyle("width: 100%; height: 100%; position: absolute;");
		
			tree = new Tree();
			tree.setStyle("border: none");
			West west = new West();
			west.appendChild(tree);
			west.setWidth("300px");
			west.setCollapsible(true);
			west.setSplittable(true);
			west.setAutoscroll(true);
			layout.appendChild(west);
			
			Center center = new Center();
			center.setFlex(true);
			center.appendChild(grid);
			layout.appendChild(center);
			
			formComponent = layout;
			tree.addEventListener(Events.ON_SELECT, this);
		}
		else
		{
			this.appendChild(grid);
			formComponent = grid;
		}
        this.appendChild(listPanel);
        listPanel.setVisible(false);
        listPanel.setWindowNo(windowNo);
    }

    public void createUI()
    {
    	if (uiCreated) return;
    	
    	uiCreated = true;
    	
    	//setup columns
    	Columns columns = new Columns();
    	grid.appendChild(columns);
    	Column col = new Column();
    	col.setWidth("14%");
    	columns.appendChild(col);
    	col = new Column();
    	col.setWidth("35%");
    	columns.appendChild(col);
    	col = new Column();
    	col.setWidth("14%");
    	columns.appendChild(col);
    	col = new Column();
    	col.setWidth("35%");
    	columns.appendChild(col);
    	col = new Column();
    	col.setWidth("2%");
    	columns.appendChild(col);
    	
    	Rows rows = new Rows();
        GridField fields[] = gridTab.getFields();
        org.zkoss.zul.Row row = new Row();                
        
        String currentFieldGroup = null;
        for (int i = 0; i < fields.length; i++)
        {
            GridField field = fields[i];
            if (field.isDisplayed())
            {
            	//included tab
            	if (field.getIncluded_Tab_ID() > 0)
            	{
            		if (row.getChildren().size() == 2)
        			{
        				row.appendChild(createSpacer());
                        row.appendChild(createSpacer());
                        row.appendChild(createSpacer());
                        rows.appendChild(row);
                        if (rowList != null)
            				rowList.add(row);
                        row = new Row();
        			} else if (row.getChildren().size() > 0) 
        			{
        				rows.appendChild(row);
        				if (rowList != null)
            				rowList.add(row);
        				row = new Row();
        			}
            		
            		row.setSpans("5");
        			row.appendChild(new Separator());
        			rows.appendChild(row);
        			
            		row = new Group();
            		row.setSpans("2,3");
            		rows.appendChild(row);
            		includedTab .put(field.getIncluded_Tab_ID(), (Group)row);
            		row = new Row();
            		continue;
            	}
            	
            	//normal field
            	String fieldGroup = field.getFieldGroup();
            	if (fieldGroup != null && fieldGroup.trim().length() > 0)
            	{
            		if (!fieldGroup.equals(currentFieldGroup)) 
            		{
            			currentFieldGroup = fieldGroup;
            			if (row.getChildren().size() == 2)
            			{
            				row.appendChild(createSpacer());
                            row.appendChild(createSpacer());
                            row.appendChild(createSpacer());
                            rows.appendChild(row);
                            if (rowList != null)
                				rowList.add(row);
                            row = new Row();
            			} else if (row.getChildren().size() > 0) 
            			{
            				rows.appendChild(row);
            				if (rowList != null)
                				rowList.add(row);
            				row = new Row();
            			}
            			
            			List<org.zkoss.zul.Row> headerRows = new ArrayList<org.zkoss.zul.Row>();
            			fieldGroupHeaders.put(fieldGroup, headerRows);
            			
            			row.setSpans("5");
            			row.appendChild(new Separator());
            			rows.appendChild(row);
            			headerRows.add(row);
            			            			
        				rowList = new ArrayList<org.zkoss.zul.Row>();
        				fieldGroupContents.put(fieldGroup, rowList);
            			            			
            			if (X_AD_FieldGroup.FIELDGROUPTYPE_Label.equals(field.getFieldGroupType())) 
            			{
            				row = new Row();            			
                			row.setSpans("4");
            				Label groupLabel = new Label(fieldGroup); 
            				row.appendChild(groupLabel);
            				row.appendChild(createSpacer());
            				rows.appendChild(row);
            				headerRows.add(row);
            				
            				row = new Row();
	                        row.setSpans("4");
	                        Separator separator = new Separator();
	                        separator.setBar(true);
	            			row.appendChild(separator);
	            			row.appendChild(createSpacer());
	            			rows.appendChild(row);
	            			headerRows.add(row);
            			}
            			else
            			{
            				row = new Group(fieldGroup);
            				if (X_AD_FieldGroup.FIELDGROUPTYPE_Tab.equals(field.getFieldGroupType()) || field.getIsCollapsedByDefault())
            				{
            					((Group)row).setOpen(false);
            				}
            				rows.appendChild(row);
            				headerRows.add(row);
            			}
            			
            			row = new Row();
            		}
            	}
            	
                if (!field.isSameLine() || field.isLongField())
                {
                	//next line
                	if(row.getChildren().size() > 0)
                	{
	                    if (row.getChildren().size() == 2)
	                    {
	                        row.appendChild(createSpacer());
	                        row.appendChild(createSpacer());
	                        row.appendChild(createSpacer());
	                    }
	                    {
	                    	row.appendChild(createSpacer());
	                    }
	                    rows.appendChild(row);
	                    if (rowList != null)
	        				rowList.add(row);
	                    row = new Row();
                	}
                } 
                else if (row.getChildren().size() == 4) 
                {
                	//next line if reach max column ( 4 )
                	row.appendChild(createSpacer());
                	rows.appendChild(row);
                    if (rowList != null)
        				rowList.add(row);
                    row = new Row();
                }

                WEditor editor = WebEditorFactory.getEditor(gridTab, field, false);
                
                if (editor != null) // Not heading
                {
                    editor.setGridTab(this.getGridTab());
                	field.addPropertyChangeListener(editor);
                    editors.add(editor);
                    editorIds.add(editor.getComponent().getUuid());
                    Div div = new Div();
                    div.setAlign("right");
                    Label label = editor.getLabel();
                    div.appendChild(label);
                    if (label.getDecorator() != null)
                    	div.appendChild(label.getDecorator());
                    row.appendChild(div);
                    row.appendChild(editor.getComponent());
                    if (field.isLongField()) {
                    	row.setSpans("1,3,1");
                    	row.appendChild(createSpacer());
                    	rows.appendChild(row);
                    	if (rowList != null)
            				rowList.add(row);
                    	row = new Row();
                    }
                    
                    if (editor instanceof WButtonEditor)
                    {
                    	if (windowPanel != null)
                    		((WButtonEditor)editor).addActionListener(windowPanel);
                    }
                    else
                    {
                    	editor.addValueChangeListener(dataBinder);
                    }
                    
                    //streach component to fill grid cell
                    if (editor.getComponent() instanceof HtmlBasedComponent) {
                    	//can't stretch bandbox & datebox
                    	if (!(editor.getComponent() instanceof Bandbox) && 
                    		!(editor.getComponent() instanceof Datebox)) {
                    		String width = "100%";    
                    		if (editor.getComponent() instanceof Button) {
                    			Button btn = (Button) editor.getComponent();
                    			String zclass = btn.getZclass();
                    			if (!zclass.contains("form-button ")) {
                    				btn.setZclass("form-button " + zclass);
                    			}
                    		} else {
                    			((HtmlBasedComponent)editor.getComponent()).setWidth(width);
                    		}
                    	}
                    }
                    
                    //setup editor context menu
                    WEditorPopupMenu popupMenu = editor.getPopupMenu();                    
                    if (popupMenu != null)
                    {
                    	popupMenu.addMenuListener((ContextMenuListener)editor);
                        this.appendChild(popupMenu);
                        if (popupMenu.isZoomEnabled() && editor instanceof IZoomableEditor) 
                        {
                        	label.setStyle("cursor: pointer; text-decoration: underline;");
                        	label.addEventListener(Events.ON_CLICK, new ZoomListener((IZoomableEditor) editor));
                        }
                        
                        label.setContext(popupMenu.getId());
                    }
                }
            }
        }
        
        //last row
        if (row.getChildren().size() > 0)
        {
            if (row.getChildren().size() == 2)
            {
                row.appendChild(createSpacer());
                row.appendChild(createSpacer());
                row.appendChild(createSpacer());
            }
            rows.appendChild(row);
            if (rowList != null)
				rowList.add(row);
        }
        grid.appendChild(rows);
        
        //create tree
        if (gridTab.isTreeTab() && tree != null) {
			int AD_Tree_ID = MTree.getDefaultAD_Tree_ID (
				Env.getAD_Client_ID(Env.getCtx()), gridTab.getKeyColumnName());
			SimpleTreeModel.initADTree(tree, AD_Tree_ID, windowNo);
        }
        
        if (!gridTab.isSingleRow())
        	switchRowPresentation();
    }

	private Component createSpacer() {
		return new Space();
	}
    
    public void dynamicDisplay (int col)
    {
        if (!gridTab.isOpen())
        {
            return;
        }
        
        //  Selective
        if (col > 0)
        	return;

        boolean noData = gridTab.getRowCount() == 0;
        logger.config(gridTab.toString() + " - Rows=" + gridTab.getRowCount());
        for (WEditor comp : editors)
        {
            GridField mField = comp.getGridField();
            if (mField != null && mField.getIncluded_Tab_ID() <= 0)
            {
                if (mField.isDisplayed(true))       //  check context
                {
                    if (!comp.isVisible())
                    {
                        comp.setVisible(true);      //  visibility
                    }
                    if (noData)
                    {
                        comp.setReadWrite(false);
                    }
                    else
                    {
                    	comp.dynamicDisplay();
                        boolean rw = mField.isEditable(true);   //  r/w - check Context
                        comp.setReadWrite(rw);
                        if (comp.getLabel() != null)
                        {
                        	comp.setMandatory(mField.isMandatory(true));    //  check context
                        }
                    }
                }
                else if (comp.isVisible())
                {
                    comp.setVisible(false);
                }
            }
        }   //  all components
        
        //hide row if all editor within the row is invisible
        List<?> rows = grid.getRows().getChildren();
        for(int i = 0; i < rows.size(); i++) 
        {
        	org.zkoss.zul.Row row = (org.zkoss.zul.Row) rows.get(i);
        	List<?> components = row.getChildren();
        	boolean visible = false;
        	boolean editorRow = false;
        	for (int j = 0; j < components.size(); j++)
        	{
        		Component component = (Component) components.get(j);
        		if (editorIds.contains(component.getUuid())) 
        		{
        			editorRow = true;
        			if (component.isVisible())
        			{
        				visible = true;
        				break;
        			}
        		}
        	}
        	if (editorRow && (row.isVisible() != visible))
        		row.setVisible(visible);
        }
        
        //hide fieldgroup if all editor row within the fieldgroup is invisible
        for(Iterator<Entry<String, List<org.zkoss.zul.Row>>> i = fieldGroupHeaders.entrySet().iterator(); i.hasNext();)
        {
        	Map.Entry<String, List<org.zkoss.zul.Row>> entry = i.next();
        	List<org.zkoss.zul.Row> contents = fieldGroupContents.get(entry.getKey());
        	boolean visible = false;
        	for (org.zkoss.zul.Row row : contents)
        	{
        		if (row.isVisible())
        		{
        			visible = true;
        			break;
        		}
        	}
        	List<org.zkoss.zul.Row> headers = entry.getValue();
        	for(org.zkoss.zul.Row row : headers)
        	{
        		if (row.isVisible() != visible)
        			row.setVisible(visible);
        	}
        }
        
        logger.config(gridTab.toString() + " - fini - " + (col<=0 ? "complete" : "seletive"));
    }   //  dynamicDisplay

    public String getDisplayLogic()
    {
        return gridTab.getDisplayLogic();
    }

    public String getTitle()
    {
        return gridTab.getName();
    } // getTitle

    /**
     * @param variableName
     */
    public String get_ValueAsString(String variableName)
    {
        return Env.getContext(Env.getCtx(), windowNo, variableName);
    } // get_ValueAsString

    /**
     * @return The tab level of this Tabpanel
     */
    public int getTabLevel()
    {
        return gridTab.getTabLevel();
    }

    public boolean isCurrent()
    {
        return gridTab != null ? gridTab.isCurrent() : false;
    }

    public int getWindowNo()
    {
        return windowNo;
    }

    public void query()
    {
        gridTab.query(false);
    }
    
    public void query (boolean onlyCurrentRows, int onlyCurrentDays, int maxRows)
    {
        gridTab.query(onlyCurrentRows, onlyCurrentDays, maxRows);
    }
    
    public GridTab getGridTab()
    {
        return gridTab;
    }
    
    public void refresh()
    {
        gridTab.dataRefresh();
    }
    
    public void activate(boolean activate)
    {
    	active = activate;
        if (listPanel.isVisible()) {
        	if (activate)
        		listPanel.activate(gridTab);
        	else
        		listPanel.deactivate();
        } else {
        	if (activate) {
        		formComponent.setVisible(activate);
        		setInitialFocus();
        	}
        }
        
        //activate embedded panel
        for(EmbeddedPanel ep : includedPanel)
        {
        	activateChild(activate, ep);
        }
    }

	private void activateChild(boolean activate, EmbeddedPanel panel) {
		if (activate)
		{
			panel.windowPanel.getADTab().evaluate(null);
			panel.windowPanel.getADTab().setSelectedIndex(0);                
			panel.tabPanel.query(false, 0, 0);
		}
		panel.tabPanel.activate(activate);
	}
    
    private void setInitialFocus() {
		for (WEditor editor : editors) {
			if (editor.isVisible() && editor.isReadWrite()) {
				Clients.response(new AuFocus(editor.getComponent()));
				break;
			}
		}
		
	}

    public void onEvent(Event event)
    {
    	if (event.getTarget() instanceof Listbox) 
    	{
    		this.switchRowPresentation();
    	} 
    	else if (event.getTarget() == tree) {
    		Treeitem item =  tree.getSelectedItem();
    		navigateTo((SimpleTreeNode)item.getValue());
    	}
    }

    private void navigateTo(SimpleTreeNode value) {
    	MTreeNode treeNode = (MTreeNode) value.getData();
    	//  We Have a TreeNode
		int nodeID = treeNode.getNode_ID();
		//  root of tree selected - ignore
		//if (nodeID == 0)
			//return;

		//  Search all rows for mode id
		int size = gridTab.getRowCount();
		int row = -1;
		for (int i = 0; i < size; i++)
		{
			if (gridTab.getKeyID(i) == nodeID)
			{
				row = i;
				break;
			}
		}
		if (row == -1)
		{
			if (nodeID > 0)
				logger.log(Level.WARNING, "Tab does not have ID with Node_ID=" + nodeID);
			return;
		}

		//  Navigate to node row
		gridTab.navigate(row);		
	}

	public void dataStatusChanged(DataStatusEvent e)
    {
    	//ignore background event
    	if (Executions.getCurrent() == null) return;
    	
        int col = e.getChangedColumn();
        logger.config("(" + gridTab + ") Col=" + col + ": " + e.toString());

        //  Process Callout
        GridField mField = gridTab.getField(col);
        if (mField != null 
            && (mField.getCallout().length() > 0 || gridTab.hasDependants(mField.getColumnName())))
        {
            String msg = gridTab.processFieldChange(mField);     //  Dependencies & Callout
            if (msg.length() > 0)
            {
                FDialog.error(windowNo, this, msg);
            }
            
            // Refresh the list on dependant fields
    		ArrayList<GridField> list = gridTab.getDependantFields(mField.getColumnName());
    		for (int i = 0; i < list.size(); i++)
    		{
    			GridField dependentField = (GridField)list.get(i);
    		//	log.trace(log.l5_DData, "Dependent Field", dependentField==null ? "null" : dependentField.getColumnName());
    			//  if the field has a lookup
    			if (dependentField != null && dependentField.getLookup() instanceof MLookup)
    			{
    				MLookup mLookup = (MLookup)dependentField.getLookup();
    				//  if the lookup is dynamic (i.e. contains this columnName as variable)
    				if (mLookup.getValidation().indexOf("@"+mField.getColumnName()+"@") != -1)
    				{
    					mLookup.refresh();
    				}
    			}
    		}   //  for all dependent fields
            
        }
        //if (col >= 0)
        if (!uiCreated)
        	createUI();
        dynamicDisplay(col);
        
        //sync tree 
        if (tree != null) {
        	if ("Deleted".equalsIgnoreCase(e.getAD_Message()))
        		if (e.Record_ID != null 
        				&& e.Record_ID instanceof Integer 
        				&& ((Integer)e.Record_ID != gridTab.getRecord_ID()))
        			deleteNode((Integer)e.Record_ID);
        		else
        			setSelectedNode(gridTab.getRecord_ID());
        	else
        		setSelectedNode(gridTab.getRecord_ID());
        }
        
        if (listPanel.isVisible()) {
        	listPanel.updateListIndex();
        	listPanel.dynamicDisplay(col);
        }
        
        if (!includedPanel.isEmpty()) {
        	for (EmbeddedPanel panel : includedPanel)
        		panel.tabPanel.query(false, 0, 0);
        }
        	
    }
    
    private void deleteNode(int recordId) {
		if (recordId <= 0) return;
		
		SimpleTreeModel model = (SimpleTreeModel) tree.getModel();
		
		if (tree.getSelectedItem() != null) {
			SimpleTreeNode treeNode = (SimpleTreeNode) tree.getSelectedItem().getValue();
			MTreeNode data = (MTreeNode) treeNode.getData();
			if (data.getNode_ID() == recordId) {
				model.removeNode(treeNode);
				return;
			}
		}
		
		SimpleTreeNode treeNode = model.find(null, recordId);
		if (treeNode != null) {
			model.removeNode(treeNode);
		} 
	}

	private void addNewNode() {
    	if (gridTab.getRecord_ID() > 0) {
	    	String name = (String)gridTab.getValue("Name");
			String description = (String)gridTab.getValue("Description");
			boolean summary = gridTab.getValueAsBoolean("IsSummary"); 
			String imageIndicator = (String)gridTab.getValue("Action");  //  Menu - Action
			//
			SimpleTreeModel model = (SimpleTreeModel) tree.getModel();
			SimpleTreeNode treeNode = model.getRoot();
			MTreeNode root = (MTreeNode) treeNode.getData();
			MTreeNode node = new MTreeNode (gridTab.getRecord_ID(), 0, name, description,
					root.getNode_ID(), summary, imageIndicator, false, null);
			SimpleTreeNode newNode = new SimpleTreeNode(node, new ArrayList<Object>());
			model.addNode(newNode);
			int[] path = model.getPath(model.getRoot(), newNode);
			Treeitem ti = tree.renderItemByPath(path);
			tree.setSelectedItem(ti);
    	}
	}

	private void setSelectedNode(int recordId) {
		if (recordId <= 0) return;
		
		if (tree.getSelectedItem() != null) {
			SimpleTreeNode treeNode = (SimpleTreeNode) tree.getSelectedItem().getValue();
			MTreeNode data = (MTreeNode) treeNode.getData();
			if (data.getNode_ID() == recordId) return;
		}
		
		SimpleTreeModel model = (SimpleTreeModel) tree.getModel();
		SimpleTreeNode treeNode = model.find(null, recordId);
		if (treeNode != null) {
			int[] path = model.getPath(model.getRoot(), treeNode);
			Treeitem ti = tree.renderItemByPath(path);
			tree.setSelectedItem(ti);
		} else {
			addNewNode();
		}
	}

	public void switchRowPresentation() {
		if (formComponent.isVisible()) {
			formComponent.setVisible(false);
		} else {
			formComponent.setVisible(true);
		}
		listPanel.setVisible(!formComponent.isVisible());
		if (listPanel.isVisible()) {
			listPanel.activate(gridTab);
		}
	}
	
	class ZoomListener implements EventListener {

		private IZoomableEditor searchEditor;

		ZoomListener(IZoomableEditor editor) {
			searchEditor = editor;
		}
		
		public void onEvent(Event event) throws Exception {
			if (Events.ON_CLICK.equals(event.getName())) {
				searchEditor.actionZoom();
			}
			
		}
		
	}

	/**
	 * Embed detail tab
	 * @param ctx
	 * @param windowNo
	 * @param gridWindow
	 * @param adTabId
	 * @param tabIndex
	 * @param tabPanel
	 */
	public void embed(Properties ctx, int windowNo, GridWindow gridWindow,
			int adTabId, int tabIndex, IADTabpanel tabPanel) {
		EmbeddedPanel ep = new EmbeddedPanel();
		ep.tabPanel = tabPanel;
		ep.adTabId = adTabId;
		includedPanel.add(ep);
		Group group = includedTab.get(adTabId);
		if (tabPanel instanceof ADTabpanel) {
			ADTabpanel atp = (ADTabpanel) tabPanel;
			atp.listPanel.setPageSize(-1);
		}
		if (group != null) {
			ADWindowPanel panel = new ADWindowPanel(ctx, windowNo, gridWindow, tabIndex, tabPanel);						
			ep.windowPanel = panel;
			org.zkoss.zul.Row row = new Row();
			row.setSpans("5");
			Component next = group.getNextSibling();
			grid.getRows().insertBefore(row, next);			
			panel.createPart(row);
			panel.getComponent().setWidth("99%");
			
			Label title = new Label(gridWindow.getTab(tabIndex).getName());
			group.appendChild(title);
			group.appendChild(panel.getToolbar());
			panel.getStatusBar().setZclass("z-group-foot");
			if (!group.isOpen()) {
				panel.getToolbar().setVisible(false);
			}
			
			panel.initPanel(-1, null);			
			if (active)
				activateChild(true, ep);
		}		
	}
	
	class EmbeddedPanel {
		ADWindowPanel windowPanel;
		IADTabpanel tabPanel;
		int adTabId;
	}
}

