<template>
  <div>
    <div style="margin-left: 30px;margin-top: 0;margin-bottom: 0; display: flex;width: auto">
      <p style="font-size: 20px">用户管理</p>
      <div style="margin-top: 20px;margin-bottom: 0px;margin-left: auto;" >
        <el-date-picker
              v-model="createDatetime"
              align="right"
              type="date"
              placeholder="选择注册日期"
              :picker-options="pickerOptions"
              @change="loadPost"
              :clearable="false"
              style="width: 150px"

        >
        </el-date-picker>
        <el-input
            placeholder="姓名"
            suffix-icon="el-icon-search"
            v-model="username" style="width: 80px;margin-left: 10px"
            @keyup.enter.native="loadPost"
        >
        </el-input>
        <el-select v-model="roleid" style="width: 120px;margin-left: 10px" @change="loadPost" placeholder="用户类型">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
        <el-input
              placeholder="请输入手机号"
              suffix-icon="el-icon-search"
              v-model="phone" style="width: 170px;margin-left: 10px"
              @keyup.enter.native="loadPost"
          >
        </el-input>
        <el-button type="success" style="margin-left: 30px" @click="add">添加</el-button>
        <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
        <el-button type="success" @click="resetParam">重置</el-button>
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

          <el-table-column width="70px">
            <template slot-scope="scope">
              <el-link type="success" @click="update(scope.row)">修改</el-link>
            </template>
          </el-table-column>
          <el-table-column width="50px">
            <template slot-scope="scope">
              <el-popconfirm
                  title="确定删除吗？"
                  @confirm="remove(scope.row)"
              >
                <el-link type="danger" slot="reference">删除</el-link>
              </el-popconfirm>
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

    <el-dialog
        title="提示"
        :visible.sync="DialogVisible"
        width="50%"
        style="margin-top: -88px"
        center>
      <el-form ref="form" :model="form" label-width="80px" style="display: grid;place-items: center;" >
        <el-form-item label="姓名" prop="username">
          <el-col :span="35">
            <el-input  v-model="form.username" style="width: 400px"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="账号" prop="account">
          <el-col :span="35">
            <el-input v-model="form.account":rows="10" style="width: 400px;"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-col :span="35">
            <el-input  v-model="form.password":rows="10" style="width: 400px;"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="性别" prop="sex">&nbsp;
          <el-col :span="35">
            <el-radio-group v-model="form.sex" :span="35" style="width: 400px;">
              <el-radio label=1 style="margin-left:80px;margin-right: 70px">男</el-radio>
              <el-radio label=0 >女</el-radio>
            </el-radio-group>
          </el-col>
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-col :span="35">
            <el-input  v-model="form.email":rows="10" style="width: 400px;"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="联系电话" prop="phone">
          <el-col :span="35">
            <el-input  v-model="form.phone":rows="10" style="width: 400px;"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="用户类型" prop="roleList">
          <el-checkbox-group v-model="selectedRoleIds" style="width: 400px;" @change="changeSelectedRoleIds()">
            <el-checkbox v-for="role in roleListOptions" :label="role.roleid"  :key="role.roleid">
              {{ role.rolename }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>




      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="resetForm">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
  </div>




</template>

<script>

export default {
  name: "Notice_Manage",
  data() {
    return{
      options: [//下拉框内容(用户类型)
        { value:1, label: '学生' },
        { value:2, label: '教师' },
        { value:3, label: '班主任'},
        { value:4, label: '教务处' },
      ],
      selectedRoleIds: [],
      roleListOptions:[
        { userList:[],roleid:1, rolename: '学生'},
        { userList:[],roleid:2, rolename: '教师' },
        { userList:[],roleid:3, rolename: '班主任',},
        { userList:[],roleid:4, rolename: '教务处' ,}],
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      DialogVisible:false,//提示窗口是否显示
      tableData: [],
      topTableData: [],
      pageSize:4,
      pageNum:1,
      total:0,
      username:'',
      phone:'',
      roleid:'',
      createDatetime:'',
      index:'',

      form:{
        userid:'',
        username:'',
        account:'',
        password:'',
        sex:'',
        email: '',
        phone: '',
        createDatetime:'',
        updateDatetime:'',
        updateUser:'',
        roleList:[],
      },

      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
    }
  },
  created() {
    this.loadPost();
  },
  methods:{
    handle_success(res) {//图片上传成功过之后将图片名字传至coverImg
      console.log(res);
      this.$message.success("图片上传成功");
      alert(res.data)
      return (this.form.coverImg = res.data);
    },
    loadPost() {
      this.$axios.post(this.$httpUrl+'/user/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          username:this.username,
          phone: this.phone,
          roleid: this.roleid,
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

    save() {
      this.form.updateUser = this.Curuser.username;
      console.log(this.form)
      if (this.form.userid) {
        this.doUpdate();
      } else {
        this.doAdd();
      }
    },
    update(user){
      this.form = user;
      this.form.updateUser = this.Curuser.username;

      this.updateSelectedRoleIds(this.form.roleList);//将form.roleList中每一项的rolid存入

      this.DialogVisible=true;

    },
    doUpdate(){


      this.$axios.post(this.$httpUrl+'/user/update',this.form).then(res=>res.data).then(res=>{
        console.log(res);

        if(res.code==200){

          this.$message({
            message: '修改成功',
            type: 'success',
          });
          this.loadPost()
          this.DialogVisible=false;

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
      this.resetForm();

    },
    add(){
      this.DialogVisible=true;
    },
    doAdd(){
      if (!this.form.username || !this.form.account || !this.form.password || !this.form.sex || !this.form.email || !this.form.phone) {
        this.$message.error('请填写完整信息');
        return;
      }
      if (!/^\d{11}$/.test(this.form.phone)) {
        this.$message.error('电话号码格式不正确');
        return;
      }
      if (!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(this.form.email)) {
        this.$message.error('邮箱格式不正确');
        return;
      }
      this.$axios.post(this.$httpUrl + '/user/save', this.form).then(res => res.data).then(res => {
        console.log(res);
        if (res.code == 200) {
          this.$message({
            message: '修改成功',
            type: 'success',
          });
          this.loadPost();
          this.DialogVisible = false;
        } else {
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
      this.resetForm();
    },
    remove(user){
      this.$axios.get(this.$httpUrl+`/user/remove?userid=${user.userid}`).then(res=>res.data).then(res=>{
        console.log(res);

        if(res.code==200){

          this.$message({
            message: '删除成功',
            type: 'success',
          });
          this.loadPost()

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
    },
    resetForm(){//重置表单信息
      this.DialogVisible = false;

      this.form = {
        userid:'',
        username:'',
        account:'',
        password:'',
        sex:'',
        email: '',
        phone: '',
        createDatetime:'',
        roleList:[]
      };
      this.selectedRoleIds = [];

      this.loadPost();

    },
    resetParam(){//重置搜索栏

      this.form = {
        userid:'',
        username:'',
        account:'',
        password:'',
        sex:'',
        email: '',
        phone: '',
        createDatetime:'',
        roleList:[]
      };
      this.loadPost();
    },
    updateSelectedRoleIds(selectedRoleIds) {//将form.roleList中的roleid都传给selectedRoleIds数组
      this.selectedRoleIds = selectedRoleIds.map(role => role.roleid);

      this.form.roleList = [];

      // 遍历roleid数组
      this.selectedRoleIds.forEach(roleId => {
        // 在roleListOptions中查找匹配的roleid
        const role = this.roleListOptions.find(option => option.roleid === roleId);
        // 如果找到了匹配的role对象，则将其添加到form.roleList中
        if (role) {
          this.form.roleList.push(role);
        }
      });
    },

    changeSelectedRoleIds() {//将selectedRoleIds中roleid对应的role存入form.roleList

      this.form.roleList = this.selectedRoleIds.map(roleId => {
        return this.roleListOptions.find(role => role.roleid === roleId);
      })
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
    beforeMount() {
      this.loadPost();
      this.loadPosttopTableData();
    },
  },

}
</script>

<style scoped>

</style>