<template>
  <div>
    <div style="margin-left: 30px;margin-top: 0;margin-bottom: 0; display: flex;width: auto">
      <p style="font-size: 20px">设备管理</p>
      <div style="margin-top: 20px;margin-bottom: 0px;margin-left: auto;" >
        <el-input
            placeholder="设备名称"
            suffix-icon="el-icon-search"
            v-model="equipmentName" style="width: 120px;margin-left: 10px"
            @keyup.enter.native="loadPost"
        >
        </el-input>
        <el-input
            placeholder="设备公司"
            suffix-icon="el-icon-search"
            v-model="equipmentCompany" style="width: 120px;margin-left: 10px"
            @keyup.enter.native="loadPost"
        >
        </el-input>
        <el-button type="success" style="margin-left: 30px" @click="add">创建</el-button>
        <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
        <el-button type="success" @click="resetParam">重置</el-button>
      </div>
    </div>
    <div style="margin-top:0px;margin-left:20px;width: 1000px;">
      <div style="width: 100%;">
        <el-table :data="tableData" :show-header="false">
          <el-table-column width="800" show-overflow-tooltip>
            <template slot-scope="scope">
              <div>
                <p>
                  设备名称：{{ scope.row.equipmentName }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  设备详情：{{ scope.row.equipmentDetial }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </p>
                <p>
                  设备公司：{{ scope.row.equipmentCompany }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  设备价格：{{ scope.row.equipmentPrice }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  设备数量：{{ scope.row.equipmentNum }}
                </p>
              </div>
            </template>
          </el-table-column>

          <el-table-column width="70px">
            <template slot-scope="scope">
              <el-link type="success" @click="update(scope.row)" style="margin-right: 20px;">修改</el-link>
            </template>
          </el-table-column>

          <el-table-column width="70px">
            <template slot-scope="scope">
              <el-popconfirm title="确定删除吗？" @confirm="remove(scope.row)">
                <el-link type="danger" slot="reference" style="margin-right: 20px;">删除</el-link>
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

        <el-form-item label="设备名称" prop="equipmentName">
          <el-col :span="35">
            <el-input v-model="form.equipmentName" style="width: 400px"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="设备公司" prop="equipmentCompany">
          <el-col :span="35">
            <el-input v-model="form.equipmentCompany" style="width: 400px"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="设备详情" prop="equipmentDetial">
          <el-input type="textarea" v-model="form.equipmentDetial" style="width: 400px"></el-input>
        </el-form-item>

        <el-form-item label="设备价格" prop="equipmentPrice">
          <el-input v-model="form.equipmentPrice" style="width: 400px"></el-input>
        </el-form-item>

        <el-form-item label="设备数量" prop="equipmentNum">
          <el-input v-model="form.equipmentNum" style="width: 400px"></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetParam">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Equipment_Manage",
  data() {
    return {
      tableData: [],
      pageSize: 4,
      pageNum: 1,
      total: 0,
      equipmentName: '',
      equipmentCompany:'',
      DialogVisible: false,
      form: {
        equipmentid: '',
        equipmentName: '',
        equipmentCompany:'',
        equipmentDetial: '',
        equipmentPrice: '',
        equipmentNum: ''
      }
    };
  },
  created() {
    this.loadPost();
  },
  methods: {
    async loadPost() {
      try {
        const res = await this.$axios.post(this.$httpUrl + '/equipment/listPage', {
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          param: {
            equipmentName: this.equipmentName,
            equipmentCompany:this.equipmentCompany
          }
        });
        if (res.data.code === 200) {
          this.tableData = res.data.data;
          this.total = res.data.total;
        } else {
          throw new Error("获取数据失败");
        }
      } catch (error) {
        console.error("数据加载失败", error);
      }
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.loadPost();
    },
    save() {
      if (this.form.equipmentid) {
        this.updateEquipment();
      } else {
        this.addEquipment();
      }
    },
    update(row) {
      this.form = { ...row };
      this.DialogVisible = true;
    },


    add() {
      this.resetParam();
      this.DialogVisible = true;
    },
    addEquipment() {
      this.$axios.post(this.$httpUrl + '/equipment/save', this.form)
          .then(res => {
            if (res.data.code === 200) {
              this.$message({
                message: '添加成功',
                type: 'success'
              });
              this.loadPost();
              this.DialogVisible = false;
            } else {
              this.$message.error('操作失败');
            }
          });
    },
    updateEquipment() {
      this.$axios.post(this.$httpUrl + '/equipment/update', this.form)
          .then(res => {
            if (res.data.code === 200) {
              this.$message({
                message: '修改成功',
                type: 'success'
              });
              this.loadPost();
              this.DialogVisible = false;
            } else {
              this.$message.error('操作失败');
            }
          });
    },
    remove(row) {
      this.$axios.get(this.$httpUrl + '/equipment/remove?equipmentid=' + row.equipmentid)
          .then(res => {
            if (res.data.code === 200) {
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.loadPost();
            } else {
              this.$message.error('操作失败');
            }
          });
    },
    resetParam() {
      this.equipmentName = '';
      this.form = {
        equipmentid: '',
        equipmentName: '',
        equipmentCompany:'',
        equipmentDetial: '',
        equipmentPrice: '',
        equipmentNum: ''
      };
      this.loadPost();
    }
  }
};
</script>

<style scoped>

</style>