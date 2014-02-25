/**
 * Copyright 2014 Lennart Koopmann <lennart@torch.sh>
 *
 * This file is part of Graylog2.
 *
 * Graylog2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog2.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package controllers.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.inject.Inject;
import controllers.*;
import lib.APIException;
import lib.ApiClient;
import lib.DateTools;
import models.ClusterService;
import models.api.responses.system.indices.IndexerFailureSummary;
import models.api.responses.system.indices.IndexerFailuresResponse;
import org.joda.time.DateTime;
import play.mvc.Result;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Lennart Koopmann <lennart@torch.sh>
 */
public class IndicesApiController extends AuthenticatedController {

    @Inject
    private ClusterService clusterService;

    public Result failures(Integer limit, Integer offset) {

        try {
            IndexerFailuresResponse failures = clusterService.getIndexerFailures(limit, offset);

            // dynatable AJAX format.
            List<Map<String, Object>> records = Lists.newArrayList();
            for (IndexerFailureSummary failure : failures.failures) {
                Map<String, Object> record = Maps.newHashMap();
                record.put("timestamp", DateTools.inUserTimeZone(DateTime.parse(failure.timestamp)).toString());
                record.put("errorMessage", failure.message);
                record.put("index", failure.index);
                record.put("deadLetter", failure.written);
                record.put("letterId", failure.letterId);

                records.add(record);
            }

            Map<String, Object> result = Maps.newHashMap();
            result.put("records", records);
            result.put("queryRecordCount", failures.total);
            result.put("totalRecordCount", failures.total);

            return ok(new Gson().toJson(result)).as("application/json");
        } catch (APIException e) {
            String message = "Could not get indexer failures. We expected HTTP 200, but got a HTTP " + e.getHttpCode() + ".";
            return status(504, views.html.errors.error.render(message, e, request()));
        } catch (IOException e) {
            return status(504, views.html.errors.error.render(ApiClient.ERROR_MSG_IO, e, request()));
        }
    }

}
