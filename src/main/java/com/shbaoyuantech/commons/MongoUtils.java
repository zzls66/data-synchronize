package com.shbaoyuantech.commons;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.shbaoyuantech.config.Configuration;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Date;
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

    public static Document findOneBy(String collection, Map<String, Object> filters) {
        Bson filter = buildBsonFilter(filters);
        return getMongoCollection(collection).find(filter).first();
    }

    public static List<Document> findManyBy(String collection, Map<String, Object> filters) {
        Bson filter = buildBsonFilter(filters);
        FindIterable<Document> iterable = getMongoCollection(collection).find(filter);
        return Lists.newArrayList(iterable);
    }

    public static ObjectId findDateObjectId(Date date) {
        if (date == null){
            return null;
        }

        String ymd = DateFormatUtils.format(date, "yyyyMMdd");
        Document doc = findOneBy("dim_date", ImmutableMap.of("date", ymd));

        return doc == null ? null : doc.getObjectId("_id");
    }

    public static Document insertOne(String collection, Map<String, Object> data){
        Document doc = new Document(data);
        getMongoCollection(collection).insertOne(doc);
        return doc;
    }

    public static void updateOne(String collection, Map<String, Object> filters, Map<String, Object> data){
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        Bson filter = buildBsonFilter(filters);
        getMongoCollection(collection).updateOne(filter, new Document("$set", new Document(data)));
    }

    private static MongoCollection<Document> getMongoCollection(String collection) {
        return db.getCollection(collection);
    }

    private static Bson buildBsonFilter(Map<String, Object> filters) {
        Bson bsonFilter = null;
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            if (bsonFilter == null) {
                bsonFilter = Filters.eq(entry.getKey(), entry.getValue());
                continue;
            }
            bsonFilter = Filters.and(bsonFilter, Filters.eq(entry.getKey(), entry.getValue()));
        }

        return bsonFilter;
    }
}
