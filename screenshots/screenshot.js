const puppeteer = require('puppeteer');
const path = require('path');

const BASE_URL = 'http://localhost:3000';
const LOGIN_URL = `${BASE_URL}/login`;

async function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function main() {
  const browser = await puppeteer.launch({
    headless: false,
    defaultViewport: { width: 1400, height: 900 }
  });

  const page = await browser.newPage();

  try {
    // 1. 登录
    console.log('正在登录...');
    await page.goto(LOGIN_URL, { waitUntil: 'networkidle2' });
    await sleep(1000);

    // 等待输入框出现
    await page.waitForSelector('.el-input__inner');

    // 输入用户名和密码
    const inputs = await page.$$('.el-input__inner');
    await inputs[0].type('admin');
    await sleep(200);
    await inputs[1].type('admin123');
    await sleep(200);

    // 点击登录按钮
    await page.click('.el-button--primary');
    await sleep(5000);

    console.log('当前页面URL:', page.url());

    // 截图登录后首页（任务列表）
    await page.waitForSelector('.el-table', { timeout: 10000 });
    await sleep(1000);
    await page.screenshot({ path: path.join(__dirname, '任务列表1.png'), fullPage: true });
    console.log('截图：任务列表1.png');

    // 2. 搁置任务页面
    console.log('正在访问搁置任务页面...');
    await page.click('.el-menu-item:nth-child(2)');
    await sleep(2000);
    await page.screenshot({ path: path.join(__dirname, '搁置任务1.png'), fullPage: true });
    console.log('截图：搁置任务1.png');

    // 3. 分组管理页面
    console.log('正在访问分组管理页面...');
    await page.click('.el-menu-item:nth-child(3)');
    await sleep(2000);
    await page.screenshot({ path: path.join(__dirname, '分组管理1.png'), fullPage: true });
    console.log('截图：分组管理1.png');

    // 4. 个人中心页面
    console.log('正在访问个人中心页面...');
    await page.click('.el-menu-item:nth-child(4)');
    await sleep(2000);
    await page.screenshot({ path: path.join(__dirname, '个人中心1.png'), fullPage: true });
    console.log('截图：个人中心1.png');

    // 5. 管理员后台页面 - 数据统计
    console.log('正在访问管理员后台...');
    await page.click('.el-menu-item:nth-child(5)');
    await sleep(2000);
    await page.screenshot({ path: path.join(__dirname, '管理员后台1.png'), fullPage: true });
    console.log('截图：管理员后台1.png（数据统计）');

    // 5.2 用户管理
    await page.click('.el-tabs__nav-scroll .el-tabs__item:nth-child(2)');
    await sleep(2000);
    await page.screenshot({ path: path.join(__dirname, '管理员后台2.png'), fullPage: true });
    console.log('截图：管理员后台2.png（用户管理）');

    // 5.3 任务列表
    await page.click('.el-tabs__nav-scroll .el-tabs__item:nth-child(3)');
    await sleep(2000);
    await page.screenshot({ path: path.join(__dirname, '管理员后台3.png'), fullPage: true });
    console.log('截图：管理员后台3.png（任务列表）');

    // 5.4 搁置任务
    await page.click('.el-tabs__nav-scroll .el-tabs__item:nth-child(4)');
    await sleep(2000);
    await page.screenshot({ path: path.join(__dirname, '管理员后台4.png'), fullPage: true });
    console.log('截图：管理员后台4.png（搁置任务）');

    // 6. 返回任务列表，截图任务列表
    await page.click('.el-menu-item:nth-child(1)');
    await sleep(2000);

    // 截图新增任务弹窗
    console.log('正在截图新增任务弹窗...');
    await page.click('.card-header .el-button--primary');
    await sleep(1000);
    await page.screenshot({ path: path.join(__dirname, '任务列表2.png'), fullPage: true });
    console.log('截图：任务列表2.png（新增任务弹窗）');

    // 关闭弹窗
    await page.click('.el-dialog__footer .el-button:first-child');
    await sleep(500);

    // 截图批量修改状态弹窗
    console.log('正在截图批量修改状态弹窗...');
    // 先勾选一些任务
    const checkboxes = await page.$$('.el-table__body .el-checkbox__input');
    if (checkboxes.length > 0) {
      await checkboxes[0].click();
      await sleep(300);
      if (checkboxes.length > 1) {
        await checkboxes[1].click();
        await sleep(300);
      }
    }
    await page.click('.batch-btn');
    await sleep(1000);
    await page.screenshot({ path: path.join(__dirname, '任务列表3.png'), fullPage: true });
    console.log('截图：任务列表3.png（批量修改状态弹窗）');

    console.log('\n截图完成！');

  } catch (error) {
    console.error('截图过程中出错:', error.message);
  } finally {
    await browser.close();
  }
}

main();