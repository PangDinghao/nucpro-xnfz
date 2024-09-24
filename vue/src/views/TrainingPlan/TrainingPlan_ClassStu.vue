<template>
  <div>
    <div style="margin-left: 30px;margin-top: 0;margin-bottom: 0; display: flex;width: auto">
      <p style="font-size: 20px">我的实训</p>
    </div>

    <div style="margin-top:0px;margin-left:20px;width: 1000px;">
      <el-table :data="TableData" :show-header="false">

        <el-table-column  >
          <template slot-scope="scope">
            <div>
              <!-- 第一行 -->
              <p>
                实训计划名：{{ scope.row.planName }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                实训包含课程
                <!-- 遍历 roleList 数组 -->
                <el-dropdown trigger="click" placement="bottom-start">
                  <i class="el-icon-arrow-down el-icon--right"></i>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="course in scope.row.courseList" :key="course.courseid">
                      {{ course.coursename }}
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                负责教师：{{ teacherName(scope.row.userid) }}
              </p>
              <!-- 第二行 -->
              <p>
                实训目标：{{scope.row.planGoal}} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                开始日期：{{scope.row.planOpentime | truncateDate}} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                结束日期：{{scope.row.planEndtime | truncateDate}} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              </p>
            </div>
          </template>
        </el-table-column>

      </el-table>

    </div>

  </div>




</template>

<script>

export default {
  name: "Notice_Manage",
  filters: {
    truncateDate(value) {//只显示固定位数的年月日
      if (value) {
        return value.substring(0, 10);
      } else {
        return '';
      }
    }
  },
  data() {
    return{
      TeacherData:[],//教师
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      PlanData: [],
      TableData:[],
    }
  },
  created() {
    this.loadPost();
    this.getTeacherUser();
  },
  updated() {
    this.loadPost();
  },
  methods:{
    //加载所有的实训计划
    loadPost() {
      this.$axios.post(this.$httpUrl+'/plan/listAll').then(res => res.data).then(res => {
        console.log(res);
        if (res.code == 200) {

          this.PlanData = res.data;
          this.total = res.total;
          if (this.Curuser.classid) {
            this.getClass();
          } else {
            console.log('classId 为空，无法调用 ');
          }

        } else {
          alert("获取数据失败");
        }
      })
    },
    //查到当前用户的班级
    getClass(){
      this.$axios.post(this.$httpUrl+'/class/list').then(res => res.data).then(res => {

        if (res.code == 200) {

          this.classList = res.data;
          this.classData = this.classList.filter(item => item.classid === this.Curuser.classid)[0];
          this.TableData = this.PlanData.filter(item => item.planid === this.classData.trainingPlanid);

        } else {
          alert("获取数据失败");
        }
      })
    },
    //获取到教师用户
    async getTeacherUser() {
      try {
        const res = await this.$axios.get(this.$httpUrl+'/user/listByRoleid?roleid=2');
        console.log("教师用户",res);
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

      this.resetForm();
      this.DialogVisible=true;
    },
  },
  computed: {
    switchValue: {
      get() {
        return this.form.sex == 1; // 将1转换为true，0转换为false
      },
      set(value) {
        this.form.sex = value ? 1 : 0; // 将true转换为1，false转换为0
      }
    },
    teacherName(userid) {//返回教师用户名字
      return (userid) => {
        if (!userid || this.TeacherData.length === 0) {
          return '';
        }
        const teacher = this.TeacherData.find(user => user.userid === userid);
        return teacher ? teacher.username : '';
      };
    },

    beforeMount() {
      this.loadPost();
      this.loadPosttopTableData();
    },
  },

}
</script>

<style scoped>

</style>