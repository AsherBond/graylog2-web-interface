/**
 * Copyright 2013 Lennart Koopmann <lennart@torch.sh>
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
package models.api.requests.dashboards;

import com.google.gson.annotations.SerializedName;
import models.User;
import models.api.requests.ApiRequest;
import models.dashboards.widgets.DashboardWidget;

import java.util.Map;

/**
 * @author Lennart Koopmann <lennart@torch.sh>
 */
public class AddWidgetRequest extends ApiRequest {

    public String type;
    public String description;
    public Map<String, Object> config;

    @SerializedName("creator_user_id")
    public String creatorUserId;

    public AddWidgetRequest(DashboardWidget widget, User user) {
        this.type = widget.getType().toString().toLowerCase();
        this.description = widget.getDescription();
        this.config = widget.getConfig();
        this.creatorUserId = user.getName();
    }

}
