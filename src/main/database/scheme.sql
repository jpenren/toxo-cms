create table page(
uuid varchar(36) NOT NULL,
content text,
customJavascript text,
customStyle text,
friendlyUrl varchar(100),
parentPage varchar(36),
hidden boolean default false,
title varchar(100),
position integer default 0,
PRIMARY KEY (uuid));
ALTER TABLE page ADD CONSTRAINT UNIQUE_FRIENDLY_URL UNIQUE(friendlyUrl);

create table site_configuration(
id integer NOT NULL,
customJavascript text,
customStyle text,
description varchar(100),
keywords varchar(100),
title varchar(100),
siteTitle varchar(100),
PRIMARY KEY (id));



