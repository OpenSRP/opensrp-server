package org.opensrp.repository;

import java.util.Date;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.Page;
import org.ektorp.PageRequest;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.joda.time.DateTime;
import org.motechproject.dao.MotechBaseRepository;

import org.opensrp.common.AllConstants;
import org.opensrp.domain.Multimedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class MultimediaRepository extends MotechBaseRepository<Multimedia> {

	@Autowired
	protected MultimediaRepository(
			@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector db) {
		super(Multimedia.class, db);
	}

	@GenerateView
	public Multimedia findByBaseEntityId(String baseEntityId) {
		List<Multimedia> files = queryView("by_baseEntityId", baseEntityId);
		if (files == null || files.isEmpty()) {
			return null;
		}
		return files.get(0);
	}

	public List<Multimedia> all(int pageSize) {
		PageRequest pageRequest = PageRequest.firstPage(pageSize);
		return db.queryForPage(createQuery("all").includeDocs(true), pageRequest, Multimedia.class).getRows();

	}
	
	 @View(name = "all_multimedia_by_ProviderId", map = "function(doc) {if (doc.type === 'Multimedia') {emit(doc.providerId);}}")
	public List<Multimedia> all(String providerId,int pageSize) {
		PageRequest pageRequest = PageRequest.firstPage(pageSize);
		return db.queryForPage(createQuery("all_multimedia_by_ProviderId").key(providerId).includeDocs(true), pageRequest, Multimedia.class).getRows();

	}
	
	 @View(name = "all_multimedia_by_contentType", map = "function(doc) {if (doc.type === 'Multimedia') {emit(doc.contentType);}}")
	public List<Multimedia> allByContent(int pageSize,String content) {
		PageRequest pageRequest = PageRequest.firstPage(pageSize);
		return db.queryForPage(createQuery("all_multimedia_by_contentType").key(content).includeDocs(true), pageRequest, Multimedia.class).getRows();

	}
	 @View(name = "all_multimedia_by_fileName", map = "function(doc) {if (doc.type === 'Multimedia') {emit(doc.fileName);}}")
	public List<Multimedia> allByName(int pageSize,String name) {
		PageRequest pageRequest = PageRequest.firstPage(pageSize);
		return db.queryForPage(createQuery("all_multimedia_by_fileName").key(name).includeDocs(true), pageRequest, Multimedia.class).getRows();
}
	 
	 @View(name = "all_multimedia_by_Date", map = "function(doc) {if (doc.type === 'Multimedia') {emit(doc.uploadDate);}}")
		public List<Multimedia> allByDate(int pageSize,DateTime startTime,DateTime endTime) {
		 	PageRequest pageRequest = PageRequest.firstPage(pageSize);
		 	return db.queryForPage(createQuery("all_multimedia_by_Date").startKey(startTime).endKey(endTime).includeDocs(true),
					pageRequest, Multimedia.class).getRows();

		}
}
