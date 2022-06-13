package com.airtel.teams.common;

import com.airtel.helper.common.ApiHelper;
import com.airtel.helper.data.AerospikeHelper;
import com.airtel.helper.data.JsonParser;
import com.airtel.helper.data.MongoHelper;
import com.airtel.helper.report.FileHelper;
import com.airtel.helper.report.ReportHelper;
import com.airtel.validator.ApiResponseValidator;
import com.airtel.validator.GenericComparator;

public class CommonFrameworkObjectStore {
    private static ApiHelper apiHelper;
    private static MongoHelper mongoHelper;
    private static AerospikeHelper aerospikeHelper;
    private static JsonParser jsonParser;
    private static ApiResponseValidator apiResponseValidator;
    private static FileHelper fileHelper;
    private static ReportHelper reportHelper;
    private static GenericComparator genericComparator;

    private CommonFrameworkObjectStore(){}

    public static ApiHelper getApiHelperObject()
    {
        if (apiHelper==null)
            apiHelper = new ApiHelper();
        return apiHelper;
    }

    public static MongoHelper getMongoHelperObject()
    {
        if (mongoHelper==null)
            mongoHelper = new MongoHelper();
        return mongoHelper;
    }

    public static AerospikeHelper getAerospikeHelperObject()
    {
        if (aerospikeHelper==null)
            aerospikeHelper = new AerospikeHelper();
        return aerospikeHelper;
    }

    public static JsonParser getJsonParserObject()
    {
        if (jsonParser==null)
            jsonParser = new JsonParser();
        return jsonParser;
    }

    public static ApiResponseValidator getApiResponseValidatorObject()
    {
        if (apiResponseValidator==null)
            apiResponseValidator = new ApiResponseValidator();
        return apiResponseValidator;
    }

    public static FileHelper getFileHelperObject()
    {
        if (fileHelper==null)
            fileHelper = new FileHelper();
        return fileHelper;
    }

    public static ReportHelper getReportHelperObject()
    {
        if (reportHelper==null)
            reportHelper = new ReportHelper();
        return reportHelper;
    }

    public static GenericComparator getGenericComparatorObject()
    {
        if (genericComparator==null)
            genericComparator = new GenericComparator();
        return genericComparator;
    }
}
