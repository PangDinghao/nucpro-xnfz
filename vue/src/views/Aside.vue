
<template>
  <el-menu default-active="2" class="el-menu-vertical-demo" background-color="#409EFF" text-color="#FFFFFF" active-text-color="#FFFFFF" router>
    <el-submenu  v-if="checkStuPermission()" index="1" >
      <template slot="title"><i class="el-icon-user"></i>学生中心</template>
      <el-menu-item @click="toNotice">新闻公告</el-menu-item>
      <el-menu-item @click="toMyCourse">课程安排</el-menu-item>
      <el-menu-item @click="toMyGrade">我的成绩</el-menu-item>
      <el-menu-item @click="toMyClass">我的班级</el-menu-item>
      <el-menu-item @click="toMyPlan">我的实训</el-menu-item>

    </el-submenu>

    <el-submenu v-if="checkPermission()" index="2">

      <template slot="title"><i class="el-icon-receiving"></i>教务管理中心</template>
      <el-menu-item @click="toNotice">新闻公告</el-menu-item>
      <el-menu-item @click="toNotice_Manage">新闻公告管理</el-menu-item>
      <el-menu-item @click="toUser_Manage">用户管理</el-menu-item>
      <el-menu-item @click="toClass_Manage">班级管理</el-menu-item>
      <el-menu-item @click="toTrainingPlan">实训计划管理</el-menu-item>
      <el-menu-item @click="toCourse_Manage">课程管理</el-menu-item>
      <el-menu-item @click="toCourse_LabManage">课程实验室安排</el-menu-item>
      <el-menu-item @click="toCourse_GradeManage">课程成绩管理</el-menu-item>
      <el-menu-item @click="toLab_Manage">实验室管理</el-menu-item>
      <el-menu-item @click="toEeuipment_Manage">设备管理</el-menu-item>
    </el-submenu>

    <el-submenu v-if="checkTeacherPermission()" index="3">

      <template slot="title"><i class="el-icon-receiving"></i>教师中心</template>
      <el-menu-item @click="toNotice">新闻公告</el-menu-item>
      <el-menu-item @click="LookMyClass">班级查看</el-menu-item>
      <el-menu-item @click="LookTrainingPlan">实训计划查看</el-menu-item>
      <el-menu-item @click="ControlCourse">课程成绩管理</el-menu-item>
    </el-submenu>

    <el-submenu v-if="checkTeacherProPermission()" index="4">

      <template slot="title"><i class="el-icon-receiving"></i>班主任中心</template>
      <el-menu-item @click="toNotice">新闻公告</el-menu-item>
      <el-menu-item @click="LookMyClass2">班级查看</el-menu-item>
      <el-menu-item @click="LookTrainingPlan">实训计划查看</el-menu-item>
    </el-submenu>
  </el-menu>

</template>


<script>
import router from "@/router";

export default {
  name: "Aside",
  created() {
    this.toNotice();
  },
  mounted() {

  },
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser')),
    }
  },
  methods: {
    checkPermission() {//检查当前用户是否为教务处
      console.log("当前用户", this.user)
      const hasPermission = this.user.roleList.some(role => role.roleid === 4);

      return hasPermission;
    },
    checkStuPermission() {//检查当前用户是否为学生
      const hasPermission = this.user.roleList.some(role => role.roleid === 1);

      return hasPermission;
    },
    checkTeacherPermission() {//检查当前用户是否为教师
      const hasPermission = this.user.roleList.some(role => role.roleid === 2);

      return hasPermission;
    },
    checkTeacherProPermission() {//检查当前用户是否为班主任
      const hasPermission = this.user.roleList.some(role => role.roleid === 3);

      return hasPermission;
    },
    toNotice() {
      if (this.$route.name == 'Notice') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Notice'});
      }
    },
    toMyGrade() {
      if (this.$route.name == 'Course_GradeByStu') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Course_GradeByStu'});
      }
    },
    toMyClass() {
      if (this.$route.name == 'Class_StuUser') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Class_StuUser'});
      }
    },
    toMyPlan() {
      if (this.$route.name == 'TrainingPlan_ClassStu') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/TrainingPlan_ClassStu'});
      }
    },
    toMyCourse() {
      if (this.$route.name == 'Course_LabByStu') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Course_LabByStu'});
      }
    },
    toNotice_Manage() {
      if (this.$route.name == 'Notice_Manage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Notice_Manage'});
      }
    },
    toUser_Manage() {
      if (this.$route.name == 'User_Manage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/User_Manage'});
      }
    },
    toClass_Manage() {
      if (this.$route.name == 'Class_Manage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Class_Manage'});
      }
    },
    toCourse_Manage() {
      if (this.$route.name == 'Course_Manage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Course_Manage'});
      }
    },
    toCourse_LabManage() {
      if (this.$route.name == 'Course_LabManage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Course_LabManage'});
      }
    },
    toCourse_GradeManage() {
      if (this.$route.name == 'Course_GradeManage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Course_GradeManage'});
      }
    },
    toTrainingPlan() {
      if (this.$route.name == 'TrainingPlan_Manage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/TrainingPlan_Manage'});
      }
    },
    toEeuipment_Manage() {
      if (this.$route.name == 'Equipment_Manage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Equipment_Manage'});
      }
    },
    toLab_Manage() {
      if (this.$route.name == 'Lab_Manage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Lab_Manage'});
      }
    },
    LookMyClass() {
      if (this.$route.name == 'Class_TeacherUser') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Class_TeacherUser'});
      }
    },
    LookMyClass2() {
      if (this.$route.name == 'Class_TeacherProUser') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Class_TeacherProUser'});
      }
    },
    LookTrainingPlan() {
      if (this.$route.name == 'TrainingPlan_ClassTeacher') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/TrainingPlan_ClassTeacher'});
      }
    },
    ControlCourse() {
      if (this.$route.name == 'Course_TeacherManage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Course_TeacherManage'});
      }
    },
    LookMyLab() {
      if (this.$route.name == 'Cause_TeacherLabManage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Cause_TeacherLabManage'});
      }
    },
    LookCourse() {
      if (this.$route.name == 'Course_TeacherProManage') {
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      } else {
        this.$router.push({path: '/Course_TeacherProManage'});
      }
    },
  }
}
</script>

<style scoped>

</style>