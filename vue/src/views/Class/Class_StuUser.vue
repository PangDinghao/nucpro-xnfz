<template>
  <div>
    <div style="align-content: center;margin-left: 30px;width: 1024px;text-align: center">

            <div style="display: flex; align-items: center; justify-content: space-between;">

              <p style="font-size: 20px">我的班级</p>
              <h1 style="margin: 0;margin-right: 50%"> 班级：{{ classData.classno }}</h1>
            </div>

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
  name: "Class_StuUser",
  data() {
    return{
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      tableData: [],
      classData: [],
      classList: [],
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

  },
  methods:{
    loadPost() {
      this.$axios.post(this.$httpUrl+'/class/list').then(res => res.data).then(res => {

        if (res.code == 200) {

          this.classList = res.data;
          this.classData = this.classList.filter(item => item.classid === this.Curuser.classid)[0];
          console.log("当前",this.classData)
          if (this.Curuser.classid) {
            this.getStuByClass();
          } else {
            console.log('classId 为空，无法调用 getStuByClass');
          }
        } else {
          alert("获取数据失败");
        }
      })
    },
    getStuByClass() {
      console.log("当前",this.classData.classid)
      this.$axios.post(this.$httpUrl+'/user/listPage', {

        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          classid: this.classData.classid,

        }

      }).then(res => res.data).then(res => {
        console.log("学生",res);
        if (res.code == 200) {

          this.tableData = res.data;
          this.total = res.total;

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
      this.$router.push("Class_Manage");
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