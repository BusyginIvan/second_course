create table points (
    id serial not null primary key check(id >= 0),
    x  float not null check(x >= -5 and x <= 5),
    y  float not null check(y >= -5 and y <= 5),
    r  float not null check(r >= 1 and r <= 3)
);
create unique index points_id_index on points (id);