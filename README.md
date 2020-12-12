# vertx-demo

一个vertx的demo，基于vertx 3.8，自定义实现了@controller、@RequestMapping、@component、
@Autowired、@Permission、@bean、@websocket等注解，及Swagger的功能，可以进行数据库的增删改查，
Redis的操作等等，其中，使用的vertx自带的MysqlClient（但是目前已过期，有新的客户端来代替了），
通过将读和写操作分成两个client来解决原来的在同一客户端下的更新缓存的问题，
自己实现的消除查询结果的笛卡儿积，将websocket的操作封装成的注解等等；

注：自己实现的上述注解功能不及Spring那般强大，仅作为个人的对此的基本实现，
如果想要vertx与spring boot进行整合，可参照: 
https://blog.csdn.net/u013615903/article/details/81776820 ， 及
https://gitee.com/jaster/vertx-spring-starter-parent 。
