<template>
  <div>
    <div style="align-content: center;margin: 20px;width: 1024px;text-align: center">

      <template>
        <div style="margin: 20px;width: 100%;text-align: center">
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <el-page-header @back="ReturnToTrainingPlan_ClassManage" content="参与班级管理" style="width: 200px;"></el-page-header>
            <h1 style="margin: 0;">{{ plan.planName }}</h1>

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
                班级编号：{{ scope.row.classno }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                班主任：{{
                  TeacherData.find(user => user.userid === scope.row.userid) ?
                      TeacherData.find(user => user.userid === scope.row.userid).username :
                      '未指定'
                }}&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                班级人数:&nbsp;{{ classNum[scope.row.classid] || '0' }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                班主任联系电话：{{
                  TeacherData.find(user => user.userid === scope.row.userid) ?
                      TeacherData.find(user => user.userid === scope.row.userid).phone :
                      '未指定' }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
  name: "TrainingPlan_ClassManage",
  data() {
    return{
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      tableData: [],
      TeacherData: [],//班主任用户
      studentData: [],//学生用户
      noplanClassDate:[],//没有实训计划的班级
      classNum: {},//班级人数
      AddClassId:[],//待添加至实训计划的班级
      pageSize:4,
      pageNum:1,
      total:0,
      classno:'',
      createDatetime:'',
      plan:JSON.parse(sessionStorage.getItem('plan')),
      trainingplan_id: '',

    }
  },
  created() {
    this.loadData();

    this.getNoPlanClass();
  },
  methods:{
    async loadData() {
      try {
        const postData = this.loadPost();
        const userData = this.getTeacher();

        await Promise.all([postData, userData]);
        console.log(this.tableData);

        for (const row of this.tableData) {
          await this.getStuNum(row.classid);
        }
      } catch (error) {
        console.error("数据加载失败", error);
      }
    },
    loadPost() {
      this.$axios.post(this.$httpUrl+'/class/listPage', {

        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          classno:this.classno,
          trainingPlanid:this.plan.planid,
          userid: this.userid,
          createDatetime:this.createDatetime.toString(),
        }

      }).then(res => res.data).then(res => {
        console.log("初始数据",res);
        if (res.code == 200) {

          this.tableData = res.data;
          this.total = res.total;

        } else {
          alert("获取数据失败");
        }
      })
    },
    async getTeacher() {
      try {
        const res = await this.$axios.get(this.$httpUrl+'/user/listByRoleid?roleid=3');//获取班主任数据
        console.log("班主任",res);
        if (res.data.code == 200) {
          this.TeacherData = res.data.data;
        } else {
          throw new Error("获取数据失败");
        }
      } catch (error) {
        console.error("获取用户数据失败", error);
        throw error;
      }
    },
    getNoPlanClass(){//查询学生用户transferData
      this.$axios.post(this.$httpUrl+'/class/list').then(res => res.data).then(res => {
        console.log("班级",res);
        if (res.code == 200) {
          this.transferData = res.data;
          this.noplanClassDate = this. transferData.filter(item => item.trainingPlanid === 0 || !item.trainingPlanid);//获取到没有实训计划号或者实训计划号为0的班级
        } else {
          alert("获取数据失败");
        }
      })
    },
    async getStuNum(classid){//获取班级人数
      try {
        const res = await this.$axios.get(this.$httpUrl+'/user/getstunum?classid='+classid);
        console.log(res);
        if (res.data.code == 200) {
          this.$set(this.classNum, classid, res.data.data);
        } else {
          throw new Error("获取数据失败");
        }
      } catch (error) {
        console.error("获取班级人数失败", error);
        throw error;
      }
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
    ReturnToTrainingPlan_ClassManage(){
      this.$router.push("TrainingPlan_ClassTeacher");
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