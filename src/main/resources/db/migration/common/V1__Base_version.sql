--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.2
-- Dumped by pg_dump version 9.5.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: chouette_info; Type: TABLE; Schema: public; Owner: nanna
--

CREATE TABLE chouette_info (
    id bigint NOT NULL,
    allow_create_missing_stop_place boolean NOT NULL,
    data_format character varying(255),
    enable_clean_import boolean NOT NULL,
    enable_stop_place_id_mapping boolean NOT NULL,
    enable_validation boolean NOT NULL,
    migrate_data_to_provider bigint,
    organisation character varying(255),
    referential character varying(255),
    cuser character varying(255),
    xmlns character varying(255),
    xmlnsurl character varying(255),
    enable_auto_import boolean default 't' NOT NULL,
    generate_dated_service_journey_ids boolean default 'f' NOT NULL,
    google_upload boolean default 'f' NOT NULL,
    google_qa_upload boolean default 'f' NOT NULL,
    enable_auto_validation boolean default 't' NOT NULL,
    enable_blocks_export boolean default 'f' NOT NULL
);


ALTER TABLE chouette_info OWNER TO nanna;

--
-- Name: provider; Type: TABLE; Schema: public; Owner: nanna
--

CREATE TABLE provider (
    id bigint NOT NULL,
    name character varying(255),
    chouette_info_id bigint
);


ALTER TABLE provider OWNER TO nanna;


create table chouette_info_service_link_modes (
    chouette_info_id bigint NOT NULL,
    transport_mode character varying(255) NOT NULL
);

--
-- Name: chouette_info_pkey; Type: CONSTRAINT; Schema: public; Owner: nanna
--

ALTER TABLE ONLY chouette_info
    ADD CONSTRAINT chouette_info_pkey PRIMARY KEY (id);

alter table chouette_info_service_link_modes
    add constraint chouette_info_service_link_modes_pk
        primary key (chouette_info_id, transport_mode);

--
-- Name: provider_pkey; Type: CONSTRAINT; Schema: public; Owner: nanna
--

ALTER TABLE ONLY provider
    ADD CONSTRAINT provider_pkey PRIMARY KEY (id);

--
-- Name: fkmfmmy1ag4cn0n5ddhajy13oh6; Type: FK CONSTRAINT; Schema: public; Owner: nanna
--

ALTER TABLE ONLY provider
    ADD CONSTRAINT fkmfmmy1ag4cn0n5ddhajy13oh6 FOREIGN KEY (chouette_info_id) REFERENCES chouette_info(id);

ALTER TABLE ONLY chouette_info_service_link_modes
    ADD CONSTRAINT chouette_info_fkey FOREIGN KEY (chouette_info_id) REFERENCES chouette_info(id) ON DELETE CASCADE;

--
-- PostgreSQL database dump complete
--

