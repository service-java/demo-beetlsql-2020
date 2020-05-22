queryOrgByUser
===

* 根据用户id查询可能所在的部门，考虑到兼职部门

```sql
select * 
from 
    core_org 
where 
    id in
    ( 
         select org_id 
         from core_user_role 
         where user_id=#userId# 
         group by org_id
    ) 
    and 1=1
    and del_flag = 0 

order by id desc
```
	
queryAllOrgCode
===

* 根据id对应的code，目前用于传递给工作流系统

```sql
select code 
from core_org 
where id  in( #join(orgIds)#)
```

