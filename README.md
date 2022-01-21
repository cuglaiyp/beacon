# beacon

beacon，中文释义“烽火”，表明该项目是一个核心功能是发送消息的平台



## 碰到的问题

**Prometheus 的坑：**

人家的 yml 格式有可能是错的，特别是换行、回车、tab等符号，所以需要自己手敲一遍。

docker 的 volume 格式 `HostPath:ContainerPath`

---

**beacon 部署到服务器的坑：**

本地启动没问题，但是在服务器上一启动，kafka 就会莫名被强制停止。通过 `docker ps -a` ->  `docker inspect kafka` -> `docker logs kafka` 都没有发现 error，只有一个 `Exited (137)`。后面不停搜索博客，有提到可能是虚拟机内存不够了，遂用 `kafka stats` 查看容器内存占用，kafka 确实很高，并且一启动 beacon，虚拟机就会很卡。遂尝试给虚拟机加内存，问题解决！

---

**curl 的坑：**

`curl -XPOST -H 'Content-Type: application/json' "localhost:8080/send"  -d '{"code":"send","messageParam":{"receiver":"15623014301","variables":{"title":"yyyyyy","contentValue":"6666164180"}},"messageTemplate
Id":1}'`

用上面这句话发送 post 请求时，服务一直报下面的错

`Content type ‘application/x-www-form-urlencoded;charset=UTF-8’ not supported`

原因是因为：

> 如果你使用Windows命令提示符来发布cURL命令，下面的命令有可能无法正常工作。你必须选择一个支持单引号参数的终端，或者使用双引号，然后转义JSON中的参数。

cmd中不允许使用单引号，因此，你需要把cURL命令换成：把单引号换成双引号，并且将JSON body中的双引号用 \" 转义。
