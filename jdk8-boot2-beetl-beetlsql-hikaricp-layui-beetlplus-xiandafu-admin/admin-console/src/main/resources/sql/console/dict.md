queryByCondition
===

```sql
select 
    @pageTag(){
    t.*
    @}
from 
    core_dict t
where 
    del_flag=0  
    @if(!isEmpty(value)) {
        and  t.value like #"%"+value+"%"#
    @}
    @if(!isEmpty(name)) {
        and  t.name like #"%"+name+"%"#
    @}
    @if(!isEmpty(typeName)) {
        and  t.type_name like #"%"+typeName+"%"#
    @}
    @if(!isEmpty(parent)) {
        and  t.parent like #"%"+parent+"%"#
    @}
    
@pageIgnoreTag() {
    order by id  desc
@}   
```

   
batchDelCoreDictByIds
===

* 批量逻辑删除

```sql
update core_dict 
set del_flag = 1 
where id in( #join(ids)#)
```
    
