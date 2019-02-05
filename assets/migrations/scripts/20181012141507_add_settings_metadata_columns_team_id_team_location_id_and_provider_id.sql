--
--    Copyright 2010-2016 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

-- // add settings metadata columns team id team location_id and provider id
-- Migration SQL that makes the change goes here.

ALTER TABLE core.settings_metadata ADD COLUMN team VARCHAR;
ALTER TABLE core.settings_metadata ADD COLUMN team_id VARCHAR;
ALTER TABLE core.settings_metadata ADD COLUMN provider_id VARCHAR;
ALTER TABLE core.settings_metadata ADD COLUMN location_id VARCHAR;

-- //@UNDO
-- SQL to undo the change goes here. 

ALTER TABLE core.settings_metadata DROP COLUMN team;
ALTER TABLE core.settings_metadata DROP COLUMN team_id;
ALTER TABLE core.settings_metadata DROP COLUMN provider_id;
ALTER TABLE core.settings_metadata DROP COLUMN location_id;
