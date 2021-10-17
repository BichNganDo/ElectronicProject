#!/bin/sh
project=ElectronicProject
local_dir=../
server_dir=/home/ubuntu
server=ubuntu@192.168.1.199


rsync -auvr $local_dir/$project/conf $server:$server_dir/$project/
rsync -auvr $local_dir/$project/public $server:$server_dir/$project/
rsync -auvr $local_dir/$project/target $server:$server_dir/$project/
rsync -auvr $local_dir/$project/templates $server:$server_dir/$project/
rsync -auvr $local_dir/$project/upload $server:$server_dir/$project/