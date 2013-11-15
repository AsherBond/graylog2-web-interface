/*
 * Copyright 2013 TORCH UG
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
 */
package models;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import lib.APIException;
import lib.ApiClient;
import models.api.requests.streams.CreateStreamRequest;
import models.api.responses.streams.GetStreamsResponse;
import models.api.responses.streams.StreamSummaryResponse;
import models.api.results.StreamsResult;
import play.mvc.Http;

import java.io.IOException;
import java.util.List;

public class StreamService {

    private final ApiClient api;
    private final Stream.Factory streamFactory;

    @Inject
    private StreamService(ApiClient api, Stream.Factory streamFactory) {
        this.api = api;
        this.streamFactory = streamFactory;
    }

    public List<Stream> all() throws IOException, APIException {
        GetStreamsResponse r = null;
        r = api.get(GetStreamsResponse.class).path("/streams").execute();

        List<Stream> streams = Lists.newArrayList();

        for (StreamSummaryResponse stream : r.streams) {
            streams.add(streamFactory.fromSummaryResponse(stream));
        }

        //return new StreamsResult(r.total, r.streams);
        return streams;
    }

    public List<Stream> allEnabled() throws IOException, APIException {
        GetStreamsResponse r = null;
        r = api.get(GetStreamsResponse.class).path("/streams/enabled").execute();

        List<Stream> streams = Lists.newArrayList();

        for (StreamSummaryResponse stream : r.streams) {
            streams.add(streamFactory.fromSummaryResponse(stream));
        }

        //return new StreamsResult(r.total, r.streams);
        return streams;
    }

    public Stream get(String streamId) throws IOException, APIException {
        StreamSummaryResponse streamResponse = null;
        streamResponse = api.get(StreamSummaryResponse.class).path("/streams/"+streamId).execute();

        return streamFactory.fromSummaryResponse(streamResponse);
    }

    public void create(CreateStreamRequest request) throws APIException, IOException {
        api.post().path("/streams").body(request).expect(Http.Status.CREATED).execute();
    }

    public void delete(String streamId) throws APIException, IOException {
        api.delete().path("/streams/" + streamId).expect(Http.Status.NO_CONTENT).execute();
    }

    public void pause(String streamId) throws APIException, IOException {
        api.post().path("/streams/" + streamId + "/pause").expect(Http.Status.OK).execute();
    }

    public void resume(String streamId) throws APIException, IOException {
        api.post().path("/streams/" + streamId + "/resume").expect(Http.Status.OK).execute();
    }
}
