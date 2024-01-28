import React from "react";
import MemberManagement from "./MemberManagement";
import RecipeManagement from "./RecipeManagement";
import Dashboard from "./Dashboard";
const ManagePage = () => {

  return(
      <div>
          <h1>관리자페이지</h1>
          <MemberManagement/>
          <RecipeManagement/>
          <Dashboard/>
      </div>
  );
}
export default ManagePage;