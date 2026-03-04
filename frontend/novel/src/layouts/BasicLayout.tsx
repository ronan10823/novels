import type { ReactNode } from "react";
import NavBar from "../components/common/Navbar";

function BasicLayout({ children }: { children: ReactNode }) {
  return (
    <>
      <div className="h-screen mx-auto max-w-160">
        <NavBar />
        <div className="p-6 h-[calc(100vh-56px)] overflow-y-scroll bg-white">
          {children}
        </div>
      </div>
    </>
  );
}
export default BasicLayout;
