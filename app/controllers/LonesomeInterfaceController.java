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
package controllers;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.inject.Inject;
import lib.ServerNodes;
import models.Node;
import play.mvc.Http;
import play.mvc.Result;

import java.util.HashMap;
import java.util.List;

public class LonesomeInterfaceController extends BaseController {

    @Inject
    private ServerNodes serverNodes;

    public Result index() {
        if (serverNodes.isConnected()) {
            return redirect(routes.DashboardController.index());
        }
        final List<Node> configuredNodes = serverNodes.getConfiguredNodes();
        final List<Node> nodesEverConnectedTo = serverNodes.all(true);

        return ok(views.html.disconnected.index.render(Http.Context.current(), configuredNodes, nodesEverConnectedTo, serverNodes));
    }

    public Result checkServerAvailability() {
        final HashMap<String,Object> map = Maps.newHashMap();
        map.put("connected", serverNodes.isConnected());
        map.put("connected_nodes_count", serverNodes.connectedNodesCount());
        map.put("total_nodes_count", serverNodes.totalNodesCount());
        return ok(new Gson().toJson(map)).as("application/json");
    }
}
