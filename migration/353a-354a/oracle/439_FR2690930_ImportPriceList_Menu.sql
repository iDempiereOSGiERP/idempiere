-- Mar 18, 2009 12:01:57 AM COT
-- FR [2690930] - Importer for Price List
INSERT INTO AD_Menu (Action,AD_Client_ID,AD_Menu_ID,AD_Org_ID,AD_Window_ID,Created,CreatedBy,EntityType,IsActive,IsReadOnly,IsSOTrx,IsSummary,Name,Updated,UpdatedBy) VALUES ('W',0,53206,0,53071,TO_DATE('2009-03-18 00:01:33','YYYY-MM-DD HH24:MI:SS'),100,'D','Y','N','N','N','Import Price List',TO_DATE('2009-03-18 00:01:33','YYYY-MM-DD HH24:MI:SS'),100)
;

-- Mar 18, 2009 12:01:57 AM COT
INSERT INTO AD_Menu_Trl (AD_Language,AD_Menu_ID, Description,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy) SELECT l.AD_Language,t.AD_Menu_ID, t.Description,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy FROM AD_Language l, AD_Menu t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Menu_ID=53206 AND EXISTS (SELECT * FROM AD_Menu_Trl tt WHERE tt.AD_Language!=l.AD_Language OR tt.AD_Menu_ID!=t.AD_Menu_ID)
;

-- Mar 18, 2009 12:01:57 AM COT
INSERT INTO AD_TreeNodeMM (AD_Client_ID,AD_Org_ID, IsActive,Created,CreatedBy,Updated,UpdatedBy, AD_Tree_ID, Node_ID, Parent_ID, SeqNo) SELECT t.AD_Client_ID,0, 'Y', SysDate, 0, SysDate, 0,t.AD_Tree_ID, 53206, 0, 999 FROM AD_Tree t WHERE t.AD_Client_ID=0 AND t.IsActive='Y' AND t.IsAllNodes='Y' AND t.TreeType='MM' AND NOT EXISTS (SELECT * FROM AD_TreeNodeMM e WHERE e.AD_Tree_ID=t.AD_Tree_ID AND Node_ID=53206)
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=0, SeqNo=0, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=218
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=0, SeqNo=1, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=153
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=0, SeqNo=2, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=263
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=0, SeqNo=3, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=166
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=0, SeqNo=4, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=203
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=0, SeqNo=5, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=236
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=0, SeqNo=6, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=183
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=0, SeqNo=7, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=160
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=0, SeqNo=8, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=278
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=0, SeqNo=9, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=345
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=0, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=222
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=1, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=223
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=2, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=340
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=3, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=185
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=4, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=53206
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=5, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=339
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=6, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=338
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=7, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=363
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=8, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=376
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=9, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=382
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=10, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=486
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=11, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=425
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=12, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=378
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=13, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=374
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=14, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=423
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=15, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=373
;

-- Mar 18, 2009 12:02:13 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=16, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=424
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=0, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=222
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=1, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=223
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=2, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=340
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=3, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=53206
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=4, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=185
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=5, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=339
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=6, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=338
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=7, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=363
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=8, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=376
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=9, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=382
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=10, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=486
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=11, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=425
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=12, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=378
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=13, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=374
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=14, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=423
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=15, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=373
;

-- Mar 18, 2009 12:02:21 AM COT
UPDATE AD_TreeNodeMM SET Parent_ID=163, SeqNo=16, Updated=SysDate WHERE AD_Tree_ID=10 AND Node_ID=424
;

