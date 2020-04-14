Trending Tags:

Select count(TRIM(t)) Count_of_Tags,trim(t) Tag from recipe_db.recipe as r, UNNEST(split(Replace(Replace(Replace(r.tags ,'[',''),']',''),'\'',''),',')) t group by t order by count(TRIM(t)) desc;


----------------------------------------------------------------------------------------------------------------------------------------

Recipe By Time Required to cook:

select count(name) as Total_Number_Of_Recipes ,
CAST(floor(minutes/10)*10 AS INT64) as Time_Required_To_Cook
from recipe_db.recipe
where minutes between 10 and 250
group by Time_Required_To_Cook
order by Time_Required_To_Cook;


---------------------------------------------------------------------------------------------------------------------------------------
Ingredient Popularity:

select ingredient,count(*) as Occurence from recipe_db.ingredients as i,recipe_db.recipe as r where STRPOS(r.ingredients,i.ingredient)>0 group by i.ingredient order by count(*) DESC LIMIT 50;

---------------------------------------------------------------------------------------------------------------------------------------
Recipe Popularity:

select count(rating) Rated_4_or_5, recipe_id Popular_Recipes from recipe_db.interactions where rating = 4 or rating = 5 group by recipe_id order by count(rating) desc limit 30;


----------------------------------------------------------------------------------------------------------------------------------------
Instant Recipes:

select count(r.id) Number_of_Instant_Recipes,r.n_steps Steps_Required from recipe_db.recipe r group by r.n_steps order by r.n_steps asc;


----------------------------------------------------------------------------------------------------------------------------------------
Recipe Contributors:

select count(name) Number_of_recipes_Posted,contributor_id Contributor from recipe_db.recipe where contributor_id in (select distinct(contributor_id) from recipe_db.recipe) group by contributor_id order by count(name) desc  limit 10;


-----------------------------------------------------------------------------------------------------------------------------------------
Healthy Posted Recipes:

select count(Healthy_Occurences) Healthy_Posted_Recipes,dt Submitted_year from (Select count(TRIM(t)) Healthy_Occurences,r.submitted dt from recipe_db.recipe as r, UNNEST(split(Replace(Replace(Replace(r.tags ,'[',''),']',''),'\'',''),',')) t 
where lower(t) like '%healthy%' or lower(t) like '%low%' or lower(t) like '%nutritious%' group by t,r.submitted
order by t)
where Healthy_Occurences <> 0
group by dt
order by count(Healthy_Occurences) desc;

