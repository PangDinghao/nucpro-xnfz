<template>
  <div>
    <div style="margin-left: 30px; margin-top: 0; margin-bottom: 0; display: flex; width: auto">
      <p style="font-size: 20px">实验室管理</p>
      <div style="margin-top: 20px; margin-bottom: 0px; margin-left: auto;">
        <el-input
            placeholder="实验室名字"
            suffix-icon="el-icon-search"
            v-model="labName"
            style="width: 120px; margin-left: 10px"
            @keyup.enter.native="loadLab"
        ></el-input>
        <el-input
            placeholder="实验室位置"
            suffix-icon="el-icon-search"
            v-model="labLocation"
            style="width: 120px; margin-left: 10px"
            @keyup.enter.native="loadLab"
        ></el-input>
        <el-button type="success" style="margin-left: 30px" @click="add">创建</el-button>
        <el-button type="primary" style="margin-left: 10px" @click="loadLab">查询</el-button>
        <el-button type="success" @click="resetParam">重置</el-button>
      </div>
    </div>
    <div style="margin-top: 0px; margin-left: 20px; width: 1000px;">
      <div style="width: 100%;">
        <el-table :data="tableData" :show-header="false">
          <el-table-column width="800px" show-overflow-tooltip>
            <template slot-scope="scope">
              <div>
                <p>
                  实验室编号：{{ scope.row.labName}} &nbsp;&nbsp;&nbsp;&nbsp;
                  实验室位置：{{ scope.row.labLocation }}&nbsp;&nbsp;&nbsp;&nbsp;
                  实验室学生人数：{{ scope.row.labStunum }}&nbsp;&nbsp;&nbsp;&nbsp;
                  实验室设备
                  <!-- 遍历 roleList 数组 -->
                  <el-dropdown trigger="click" placement="bottom-start">
                    <i class="el-icon-arrow-down el-icon--right"></i>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-for="equip in scope.row.equipmentList" :key="equip.equipmentid">
                        {{ equip.equipmentName }}
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </p>
              </div>
            </template>
          </el-table-column>

          <el-table-column width="70px">
            <template slot-scope="scope">
              <el-link type="success" @click="update(scope.row)" style="margin-right: 10px;">修改</el-link>
            </template>
          </el-table-column>

          <el-table-column width="70px">
            <template slot-scope="scope">
              <el-popconfirm title="确定删除吗？" @confirm="removeLab(scope.row)">
                <el-link type="danger" slot="reference" style="margin-right: 10px;">删除</el-link>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div style="display: flex; justify-content: center;">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[4, 5, 10, 20]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
        ></el-pagination>
      </div>
    </div>

    <el-dialog
        title="提示"
        :visible.sync="DialogVisible"
        width="50%"
        style="margin-top: -88px"
        center
    >
      <el-form ref="form" :model="form" label-width="180px" style="display: grid; place-items: center;">
        <el-form-item label="实验室名字" prop="labName">
          <el-col :span="35">
            <el-input v-model="form.labName" style="width: 400px"></el-input>
          </el-col>
        </el-form-item><el-form-item label="实验室位置" prop="labLocation">
          <el-col :span="35">
            <el-input v-model="form.labLocation" style="width: 400px"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="实验室学生人数" prop="labStunum">
          <el-col :span="35">
            <el-input v-model="form.labStunum" style="width: 400px"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="包含设备" prop="equiplist">
          <el-select v-model="selectedEquipmentIds"  filterable multiple  placeholder="请选择实验设备" @change="changeSelectedEquipmentIds()">
            <el-option
                v-for="equip in equipDate"
                :key="equip.equipmentid"
                :label="equip.equipmentName"
                :value="equip.equipmentid">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetParam">取消</el-button>
        <el-button type="primary" @click="doUpdate">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "LabManage",
  data() {
    return {
      tableData: [],
      pageSize: 4,
      pageNum: 1,
      total: 0,
      labName: "",
      labLocation: "",
      DialogVisible: false,
      equipDate: [],//设备初始数据
      selectedEquipmentIds:[],//待添加/修改的设备数据
      form: {
        labid:"",
        labName:"",
        labLocation:"",
        labStunum:"",
        equipmentList:[],
      }
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData(){
      this.loadLab();
      this.getEquipList();
    },
    async loadLab() {
      try {
        const res = await this.$axios.post(this.$httpUrl + "/lab/listPage", {
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          param: {
            labName: this.labName,
            labLocation: this.labLocation
          }
        });
        if (res.data.code === 200) {
          this.tableData = res.data.data;
          this.total = res.data.total;
          console.log("实验室初始",this.tableData)
        } else {
          throw new Error("获取数据失败");
        }
      } catch (error) {
        console.error("数据加载失败", error);
      }
    },
    async getEquipList() {
      try {
        const res = await this.$axios.get(this.$httpUrl + "/equipment/ListAll");
        if (res.data.code === 200) {
          this.equipDate = res.data.data;
          console.log("设备初始", res.data)
        } else {
          throw new Error("获取数据失败");
        }
      } catch (error) {
        console.error("数据加载失败", error);
      }
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.loadLab();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.loadLab();
    },
    update(lab){
      console.log("表单数据",lab)
      this.DialogVisible=true;
      this.form = lab;
      this.updateSelectedEquipmentIds(this.form.equipmentList);//将form.equipmentList中每一项的equipmentid存入

      this.loadLab();
    },
    add(){
      this.resetForm();
      this.DialogVisible=true;

    },
    doUpdate() {
      if (this.form.labid) {
        this.updateLab();
      } else {
        this.submitLab();
      }
    },
    submitLab() {
      this.$axios
          .post(this.$httpUrl + "/lab/save", this.form)
          .then((res) => {
            if (res.data.code === 200) {
              this.$message({
                message: "添加成功",
                type: "success"
              });
              this.loadLab();
              this.DialogVisible = false;
            } else {
              this.$message.error("操作失败");
            }
          });
    },
    updateLab() {
      this.$axios
          .post(this.$httpUrl + "/lab/update", this.form)
          .then((res) => {
            if (res.data.code === 200) {
              this.$message({
                message: "修改成功",
                type: "success"
              });
              this.loadLab();
              this.DialogVisible = false;
            } else {
              this.$message.error("操作失败");
            }
          });
    },
    removeLab(row) {
      this.$axios
          .get(this.$httpUrl + "/lab/remove?labid=" + row.labid)
          .then((res) => {
            if (res.data.code === 200) {
              this.$message({
                message: "删除成功",
                type: "success"
              });
              this.loadLab();
            } else {
              this.$message.error("操作失败");
            }
          });
    },
    resetParam() {
      this.labLocation = "";
      this.form = {
        labid: "",
        labLocation: "",
        labStunum: ""
      };
      this.loadLab();
    },
    resetForm(){//重置表单信息
      this.DialogVisible = false;

      this.form = {
        labid: "",
        labName: "",
        labLocation: "",
        labStunum: ""
      };
      this.selectedEquipmentIds = [];

      this.loadData();

    },


    updateSelectedEquipmentIds(EquipIds) {//将form.courseList中的courseid都传给selectedCourseIds数组
      this.selectedEquipmentIds = EquipIds.map(equipment => equipment.equipmentid);


      this.form.equipmentList = [];

      // 遍历courseid数组
      this.selectedEquipmentIds.forEach(equipmentId => {
        // 在equipDate中查找匹配的equipmentid
        const equipment = this.equipDate.find(option => option.equipmentid ===  EquipIds);
        // 如果找到了匹配的course对象，则将其添加到form.courseList中
        if (equipment) {
          this.form.equipmentList.push(equipment);
        }
      });
    },
    changeSelectedEquipmentIds() {
      this.form.equipmentList = this.selectedEquipmentIds.map(equipmentId => {
        return this.equipDate.find(equipment => equipment.equipmentid === equipmentId);
      })
    },
  }
};
</script>

<style scoped>

</style>
