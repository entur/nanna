insert into chouette_info(id, xmlns, xmlnsurl, referential, organisation, cuser,allow_create_missing_stop_place, enable_auto_import, enable_auto_validation, enable_blocks_export, generate_dated_service_journey_ids) values (1, 'flybussekspressen', 'http://www.ns.1','flybussekspressen', 'Rutebanken', 'admin@rutebanken.org',true,true,true, false, false);
insert into chouette_info(id, xmlns, xmlnsurl, referential, organisation, cuser,allow_create_missing_stop_place, enable_auto_import, enable_auto_validation, enable_blocks_export, generate_dated_service_journey_ids) values (2, 'flybussekspressen2', 'http://www.ns.2','flybussekspressen2', 'Rutebanken2', 'admin2@rutebanken.org',true,true,true, false, false);
insert into chouette_info(id, xmlns, xmlnsurl, referential, organisation, cuser,allow_create_missing_stop_place, enable_auto_import, enable_auto_validation, enable_blocks_export, generate_dated_service_journey_ids) values (3, 'rb_flybussekspressen', 'http://www.ns.2','rb_flybussekspressen', 'Rutebanken', 'admin2@rutebanken.org',true,true,true, false, false);

insert into provider(id, name, chouette_info_id) values (42, 'Flybussekspressen', 1);
insert into provider(id, name, chouette_info_id) values (43, 'Flybussekspressen2', 2);
insert into provider(id, name, chouette_info_id) values (44, 'rb_Flybussekspressen', 3);
