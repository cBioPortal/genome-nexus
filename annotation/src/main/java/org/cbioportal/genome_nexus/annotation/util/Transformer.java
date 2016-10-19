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

package org.cbioportal.genome_nexus.annotation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Selcuk Onur Sumer
 */
public class Transformer
{
    /**
     * Transforms the given jsonString into a list of DBObject instances.
     *
     * @param jsonString    json string
     * @return List of DBObject instances
     */
    public static List<DBObject> convertToDbObject(String jsonString)
    {
        List<DBObject> dbObjects = new ArrayList<>();
        DBObject dbObject = (DBObject) JSON.parse(jsonString);

        // if it is a list, just add all into the list
        if (dbObject instanceof List)
        {
            for (Object obj: ((List) dbObject))
            {
                dbObjects.add((DBObject) obj);
            }
        }
        else
        {
            dbObjects.add(dbObject);
        }

        return dbObjects;
    }

    /**
     * Maps the given raw JSON string onto the provided class instance.
     *
     * @param jsonString    raw JSON string
     * @return a list of instances of the provided class
     * @throws IOException
     */
    public static <T> List<T> mapJsonToInstance(String jsonString, Class<T> type) throws IOException
    {
        List<T> list = new ArrayList<>();

        for (DBObject dbObject: convertToDbObject(jsonString))
        {
            String toMap = JSON.serialize(dbObject);
            ObjectMapper mapper = new ObjectMapper();

            // map json string onto the given class type
            list.add(mapper.readValue(toMap, type));
        }

        return list;
    }
}
