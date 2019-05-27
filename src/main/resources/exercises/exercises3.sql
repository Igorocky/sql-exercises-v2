/*====================================
id=2019-05-27--1 schema=shapes gen=shapes

Query for one kind of data

Select id and area of each rectangle (area=width*height). order by id.
*/----------------------------------
select id, width*height area
from shape
where kind = 'rectangle'
order by id

/*====================================
id=2019-05-27--2 schema=shapes gen=shapes

Query for different kind of data

Select id, kind and area of each shape (area_of_rectangle=width*height, area_of_circle=3.14*radius*radius). order by id.
*/----------------------------------
select id, kind, case when kind='rectangle' then width*height when kind='circle' then 3.14*radius*radius end area
from shape
order by id

/*====================================
id=2019-05-27--3 schema=shapes gen=shapes

Query for different kind of data and filter by calculation

For shapes with area less than 10, select id, kind and area. order by id.
*/----------------------------------
select id, kind, case when kind='rectangle' then width*height when kind='circle' then 3.14*radius*radius end area
from shape
where 10 > case when kind='rectangle' then width*height when kind='circle' then 3.14*radius*radius end
order by id
