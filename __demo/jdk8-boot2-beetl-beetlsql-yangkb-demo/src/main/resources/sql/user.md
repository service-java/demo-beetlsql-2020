sample
===
* 注释

	select #use("cols")# from user  where  #use("condition")#

cols
===
	id,name,department_id,create_time,password

updateSample
===
	
	id=#id#,name=#name#,department_id=#departmentId#,create_time=#createTime#,password=#password#

condition
===

	1 = 1  
	@if(!isEmpty(id)){
	 and id=#id#
	@}
	@if(!isEmpty(name)){
	 and name=#name#
	@}
	@if(!isEmpty(departmentId)){
	 and department_id=#departmentId#
	@}
	@if(!isEmpty(createTime)){
	 and create_time=#createTime#
	@}
	@if(!isEmpty(password)){
	 and password=#password#
	@}
	
	