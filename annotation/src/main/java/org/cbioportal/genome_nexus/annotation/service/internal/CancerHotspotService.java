/*
 * Copyright (c) 2016 Memorial Sloan-Kettering Cancer Center.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS
 * FOR A PARTICULAR PURPOSE. The software and documentation provided hereunder
 * is on an "as is" basis, and Memorial Sloan-Kettering Cancer Center has no
 * obligations to provide maintenance, support, updates, enhancements or
 * modifications. In no event shall Memorial Sloan-Kettering Cancer Center be
 * liable to any party for direct, indirect, special, incidental or
 * consequential damages, including lost profits, arising out of the use of this
 * software and its documentation, even if Memorial Sloan-Kettering Cancer
 * Center has been advised of the possibility of such damage.
 */

/*
 * This file is part of cBioPortal Genome Nexus.
 *
 * cBioPortal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.cbioportal.genome_nexus.annotation.service.internal;

import org.cbioportal.genome_nexus.annotation.domain.Hotspot;
import org.cbioportal.genome_nexus.annotation.service.HotspotService;
import org.cbioportal.genome_nexus.annotation.util.Transformer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author Selcuk Onur Sumer
 */
@Service
public class CancerHotspotService implements HotspotService
{
    // TODO make the cache functional
    private HotspotCache cache;

    private String hotspotsURL;
    @Value("${hotspots.url}")
    public void setHotspotsURL(String hotspotsURL) { this.hotspotsURL = hotspotsURL; }

    public CancerHotspotService()
    {
        this.cache = new HotspotCache();
    }

    @Override
    public List<Hotspot> getHotspots(String transcriptId)
    {
        try
        {
            return Transformer.mapJsonToInstance(getHotspotsJSON(transcriptId), Hotspot.class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Hotspot> getHotspots()
    {
        try
        {
            return Transformer.mapJsonToInstance(getHotspotsJSON(null), Hotspot.class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private String getHotspotsJSON(String variables)
    {
        String uri = hotspotsURL;

        if (variables != null &&
            variables.length() > 0)
        {
            uri += "/" + variables;
        }

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, String.class);
    }
}
