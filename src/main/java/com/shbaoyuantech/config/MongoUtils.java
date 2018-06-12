package com.shbaoyuantech.config;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.shbaoyuantech.commons.CompanyConstant;
import com.shbaoyuantech.commons.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoUtils {
    private static Logger logger = LoggerFactory.getLogger(MongoUtils.class);

    private static MongoDatabase db;
    static {
        try {
            Configuration config = Configuration.getInstance();
            MongoClient mongoClient = new MongoClient(config.getMongoHost(), config.getMongoPort());
            db = mongoClient.getDatabase(config.getMongoDatebase());
        } catch (Exception e) {
            logger.error("Error", e);
        }
    }

    private static Document findOneBy(int rowId, String collectionName, int company){
        Bson filter = Filters.eq("rowId", rowId);
        if (CompanyConstant.COMMON_FLAG == 0) {
            filter = Filters.and(filter, Filters.eq("companyId", company));
        }
        MongoCollection<Document> coll = getCollection(collectionName);
        return coll.find(filter).first();

//        Document source = null;
//        FindIterable<Document> iterable;
//
//        if(Integer.compare(CompanyConstant.COMMON_FLAG, company) == -1){
//            iterable = coll.find(Filters.and(Filters.eq("rowId", rowId), Filters.eq("companyId", company)));
//        }else {
//            iterable = coll.find(Filters.eq("rowId", rowId));
//        }
//
//        MongoCursor<Document> iterator = iterable.iterator();
//        if(iterator.hasNext()){
//            source = iterator.next();
//        }
//        return source;
    }

//    public static List<Document> findBy(String collName, Map<String, Object> filters) {
//
//    }

    public static List<Document> accessDocumentsByObjectId(String fieldName, ObjectId objectId, String collectionName, int company){
        List<Document> list = new ArrayList<>();
        FindIterable<Document> iterable;
        MongoCollection<Document> coll = db.getCollection(collectionName);

        if(Integer.compare(CompanyConstant.COMMON_FLAG, company) == -1){
            iterable = coll.find(Filters.and(Filters.eq(fieldName, objectId), Filters.eq("companyId", company)));
        }else {
            iterable = coll.find(Filters.eq(fieldName, objectId));
        }

        MongoCursor<Document> iterator = iterable.iterator();
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        return list;
    }

//    findOneByObjectId()

    public static Object accessFieldByCurrentObjectId(String fieldName, String collectionName, ObjectId objectId){
        Object result = null;

        MongoCollection<Document> coll = getCollection(collectionName);
        FindIterable<Document> iterable = coll.find(Filters.eq("_id", objectId));
        MongoCursor<Document> iterator = iterable.iterator();
        if(iterator.hasNext()){
            Document source = iterator.next();
            result = source.get(fieldName);
        }
        return result;
    }

    public static ObjectId accessCurrentObjectId(int rowId, String collectionName, int company){
        Document source = findOneBy(rowId, collectionName, company);
        return source == null ? null : source.getObjectId("_id");
    }

    static ObjectId accessDateObjectId(String dateStr){
        MongoCollection<Document> collection = db.getCollection("dim_date");
        if (StringUtils.isEmpty(dateStr)){
            return null;
        }

        Document source = null;
        dateStr = DateUtils.accessYYYYMMDDataStr(dateStr);
        FindIterable<Document> iterable = collection.find(Filters.eq("date", dateStr));
        MongoCursor<Document> iterator = iterable.iterator();
        if(iterator.hasNext()){
            source = iterator.next();
        }
        return source == null ? null : source.getObjectId("_id");
    }

    private static boolean isDocumentNotNull(FindIterable<Document> documents){
        MongoCursor<Document> iterator = documents.iterator();
        return iterator.hasNext();
    }

    public static boolean checkIdempotent(Map<String, Object> data, String collectionName, int company){
        MongoCollection<Document> collection = db.getCollection(collectionName);

        FindIterable<Document> existStore;
        if(Integer.compare(company, CompanyConstant.COMMON_FLAG) == 0){
            existStore = collection.find(Filters.eq("rowId", data.get("rowId")));
        }else{
            existStore = collection.find(Filters.and(Filters.eq("rowId", data.get("rowId")), Filters.eq("companyId", company)));
        }
        return isDocumentNotNull(existStore);
    }

    public static Document insertOne(Map<String, Object> data, String collectionName){
//        if(CollectionUtils.isEmpty(data)){
//            return;
//        }
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document doc = new Document(data);
        collection.insertOne(doc);

        return doc;
    }

//    public static void updateOne(String collectionName, Map<String, Object> filter, Map<String, Object> data){
    public static void updateOne(Map<String, Object> data, String collectionName, int id, int company){
        if(CollectionUtils.isEmpty(data)){
            return;
        }
        MongoCollection<Document> collection = db.getCollection(collectionName);
        if(Integer.compare(company, CompanyConstant.COMMON_FLAG) == 0){
            collection.updateMany(Filters.eq("rowId", id), new Document("$set", new Document(data)));
        }else{
            collection.updateMany(Filters.and(Filters.eq("rowId", id), Filters.eq("companyId", company)), new Document("$set", new Document(data)));
        }
    }

    private static MongoCollection<Document> getCollection(String collName) {
        return db.getCollection(collName);
    }
}
