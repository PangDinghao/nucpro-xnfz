<template>
  <div>
    <div style="align-content: center;margin: 20px;width: 1024px;text-align: center">

      <template>
        <div style="margin: 20px;width: 100%;text-align: center">
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <el-page-header @back="ReturnToClassManage" content="班级管理" style="width: 200px;"></el-page-header>
            <h1 style="margin: 0;">{{ class1.classno }}</h1>
          </div>
        </div>
      </template>

    </div>

    <div style="margin-top:0px;margin-left:20px;width: 1000px;">
      <el-table :data="tableData" :show-header="false">

        <el-table-column  width="880px">
          <template slot-scope="scope">
            <div>
              <!-- 第一行 -->
              <p>
                用户名：{{ scope.row.username }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                性别：
                <el-tag
                    :type="scope.row.sex == 1 ?  'success' : 'primary'"
                    disable-transitions>{{scope.row.sex== 1 ? '男' : '女'}}</el-tag>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                联系电话：{{ scope.row.phone }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                邮箱:{{ scope.row.email }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                最近修改日期{{scope.row.updateDatetime}}

              </p>
              <!-- 第二行 -->
              <p>
                账号：{{scope.row.account}} &nbsp;&nbsp;&nbsp;&nbsp;
                密码：{{scope.row.password}} &nbsp;&nbsp;&nbsp;&nbsp;
                注册日期：{{scope.row.createDatetime}} &nbsp;&nbsp;&nbsp;&nbsp;
                用户类型：
                <!-- 遍历 roleList 数组 -->
                <span v-if="scope.row.roleList && scope.row.roleList.length > 0">
                   <span v-for="(role, index) in scope.row.roleList" :key="index">
                       <!-- 显示每个角色对象的 rolename -->
                      {{ role.rolename }}
                     <!-- 如果不是最后一个角色对象，则显示分隔符 -->
                      <template v-if="index !== scope.row.roleList.length - 1">| </template>
                   </span>
                </span>
                <span v-else>无类型</span>&nbsp;&nbsp;&nbsp;&nbsp;
                最近修改者:{{scope.row.updateUser}}
              </p>
            </div>
          </template>
        </el-table-column>





      </el-table>
      <div style="display: flex; justify-content: center;">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page= "pageNum"
            :page-sizes="[4,5, 10, 20]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>




</template>

<script>

export default {
  name: "Class_TeacherUserManger",
  data() {
    return{
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      tableData: [],
      studentData: [],//学生用户
      noclassStudentDatea:[],//班级为0的学生
      AddStudentId:[],//待添加至班级的用户
      pageSize:4,
      pageNum:1,
      total:0,
      username:'',
      createDatetime:'',
      class1:JSON.parse(sessionStorage.getItem('class')),
      classid: '',

    }
  },
  created() {
    this.loadPost();
    this.getNoClassUser();
  },
  methods:{
    loadPost() {
      this.$axios.post(this.$httpUrl+'/user/listPage', {

        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          username:this.username,
          phone: this.phone,
          roleid: this.roleid,
          classid: this.class1.classid,
          createDatetime:this.createDatetime.toString(),
        }

      }).then(res => res.data).then(res => {
        console.log(res);
        if (res.code == 200) {

          this.tableData = res.data;
          this.total = res.total;

        } else {
          alert("获取数据失败");
        }
      })
    },
    getNoClassUser(){//查询学生用户transferData
      this.$axios.get(this.$httpUrl+'/user/listByRoleid?roleid=1').then(res => res.data).then(res => {
        console.log("学生用户",res);
        if (res.code == 200) {
          this.transferData = res.data;
          this.noclassStudentDatea = this. transferData.filter(item => item.classid === 0 || !item.classid);//获取到没有班级号或者班级号为0的班级
        } else {
          alert("获取数据失败");
        }
      })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize=val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val;
      this.loadPost();
    },
    add(){
      this.$axios.post(this.$httpUrl+'/user/updateToClass', {
        AddStudentId : this.AddStudentId,
        classid : this.class1.classid,

      }).then(res=>res.data).then(res=>{
        console.log(res);
        if(res.code==200){
          this.loadPost()
          this.getNoClassUser()
          this.AddStudentId ='';
          this.$message({
            message: '添加成功',
            type: 'success',
          });

        }else{
          this.$message({
            message: '请选择用户',
            type: 'error'
          });
        }
      })
    },
    removeOutClass(userid){//将用户的classid置0（移出班级）
      const userIdArray = [userid];
      this.$axios.post(this.$httpUrl+'/user/updateToClass', {
        AddStudentId : userIdArray,
        classid : '0',

      }).then(res=>res.data).then(res=>{
        console.log(res);
        if(res.code==200){
          this.loadPost()
          this.getNoClassUser()
          this.AddStudentId ='';
          this.$message({
            message: '修改成功',
            type: 'success',
          });

        }else{
          this.$message({
            message: '修改失败',
            type: 'error'
          });
        }
      })
    },

    ReturnToClassManage(){
      this.$router.push("Class_TeacherUser");
    }
  },
  computed: {
    beforeMount() {
      this.loadPost();
      this.loadPosttopTableData();
    },
  },

}
</script>

<style scoped>

</style>