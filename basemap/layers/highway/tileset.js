/**
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 or implied. See the License for the specific language governing permissions and limitations under
 the License.
 **/
export default {
    id: 'highway',
    queries: [
        {
            minzoom: 4,
            maxzoom: 6,
            sql:
                "SELECT id, tags, geom FROM osm_highway_z$zoom WHERE tags ->> 'highway' IN ( 'motorway')",
        },
        {
            minzoom: 6,
            maxzoom: 9,
            sql:
                "SELECT id, tags, geom FROM osm_highway_z$zoom WHERE tags ->> 'highway' IN ( 'motorway',  'trunk',  'primary')",
        },
        {
            minzoom: 9,
            maxzoom: 10,
            sql:
                "SELECT id, tags, geom FROM osm_highway_z$zoom WHERE tags ->> 'highway' IN ( 'motorway', 'motorway_link',  'trunk', 'trunk_link',  'primary', 'primary_link',  'secondary', 'secondary_link')",
        },
        {
            minzoom: 10,
            maxzoom: 11,
            sql:
                "SELECT id, tags, geom FROM osm_highway_z$zoom WHERE tags ->> 'highway' IN ( 'motorway', 'motorway_link',  'trunk', 'trunk_link',  'primary', 'primary_link',  'secondary', 'secondary_link',  'tertiary', 'tertiary_link')",
        },
        {
            minzoom: 11,
            maxzoom: 14,
            sql:
                "SELECT id, tags, geom FROM osm_highway_z$zoom WHERE tags ->> 'highway' IN ( 'motorway', 'motorway_link',  'trunk', 'trunk_link',  'primary', 'primary_link',  'secondary', 'secondary_link',  'tertiary', 'tertiary_link', 'unclassified', 'residential')",
        },
        {
            minzoom: 14,
            maxzoom: 20,
            sql:
                "SELECT id, tags, geom FROM osm_ways_z$zoom WHERE tags ? 'highway'",
        },
    ],
}