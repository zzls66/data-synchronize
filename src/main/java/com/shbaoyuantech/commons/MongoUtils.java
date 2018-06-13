package com.shbaoyuantech.commons;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.shbaoyuantech.config.Configuration;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

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

    private static Document findOneBy(String collName, int companyId, Map<String, Object> filters){
        Bson filter = getFilter(filters, companyId);
        MongoCollection<Document> collection = getCollection(collName);
        return collection.find(filter).first();
    }

    static List<Document> findManyBy(String collName, int companyId, Map<String, Object> filters){
        Bson filter = getFilter(filters, companyId);
        MongoCollection<Document> collection = getCollection(collName);

        FindIterable<Document> iterable = collection.find(filter);
        return Lists.newArrayList(iterable);
    }

    static Object findFieldValueBy(String collName, String fieldName, Map<String, Object> filters){
        Document doc = findOneBy(collName, (int)filters.get("companyId"), filters);
        if(doc == null){
            return null;
        }
        return doc.get(fieldName);
    }

    static ObjectId findObjectId(String collName, int companyId, Map<String, Object> filters){
        Document doc = findOneBy(collName, companyId, filters);
        return doc.getObjectId("_id");
    }

    static ObjectId findDateObjectId(String dateStr){
        if (StringUtils.isEmpty(dateStr)){
            return null;
        }
        MongoCollection<Document> collection = getCollection("dim_date");
        dateStr = DateUtils.accessYYYYMMDDataStr(dateStr);
        Document doc = collection.find(Filters.eq("date", dateStr)).first();

        return doc == null ? null : doc.getObjectId("_id");
    }

    public static boolean checkIdempotency(String collName, int companyId, Map<String, Object> data){
        Document doc = findOneBy(collName, companyId, data);
        return doc == null;
    }

    public static Document insertOne(String collName, Map<String, Object> data){
        MongoCollection<Document> collection = getCollection(collName);
        Document doc = new Document(data);
        collection.insertOne(doc);
        return doc;
    }

    public static void updateOne(String collectionName, Map<String, Object> filters, Map<String, Object> data){
        if(CollectionUtils.isEmpty(data)){
            return;
        }
        MongoCollection<Document> collection = getCollection(collectionName);
        Bson filter = getFilter(filters, (int) filters.get("companyId"));
        collection.updateMany(filter, new Document("$set", new Document(data)));
    }

    private static MongoCollection<Document> getCollection(String collName) {
        return db.getCollection(collName);
    }

    private static Bson getFilter(Map<String, Object> filters){
        Bson filter = null;
        for(Map.Entry<String, Object> entry : filters.entrySet()){
            if(filter == null){
                filter = Filters.eq(entry.getKey(), entry.getValue());
                continue;
            }
            filter = Filters.and(filter, Filters.eq(entry.getKey(), entry.getValue()));
        }
        return filter;
    }
    private static Bson getFilter(Map<String, Object> filters, int companyId){
        Bson filter = getFilter(filters);
        if(CompanyType.COMMON < companyId){
            filter = Filters.and(filter, Filters.eq("companyId", companyId));
        }
        return filter;
    }

}
