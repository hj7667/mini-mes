import Link from "next/link";
import { Factory, LayoutDashboard, Search } from "lucide-react";

const menus = [
  {
    href: "/worker",
    icon: Factory,
    title: "작업자 키오스크",
    desc: "현장 실적 등록 및 설비 시뮬레이터",
  },
  {
    href: "/admin",
    icon: LayoutDashboard,
    title: "관리자 대시보드",
    desc: "공정별 가동 현황 및 LOT 요약",
  },
  {
    href: "/lot-trace",
    icon: Search,
    title: "LOT 역추적",
    desc: "Time Machine 타임라인 뷰어",
  },
];

export default function Home() {
  return (
    <div className="min-h-screen bg-[#0b0f14] text-white flex flex-col items-center justify-center p-6">
      <div className="text-emerald-400 font-bold text-2xl mb-1">FactoryFlow</div>
      <div className="text-white/40 mb-10">산업용 모터 제조 시뮬레이션 MES</div>

      <div className="grid grid-cols-3 gap-5 w-full max-w-4xl">
        {menus.map(({ href, icon: Icon, title, desc }) => (
          <Link
            key={href}
            href={href}
            className="bg-[#111823] border border-white/10 rounded-xl p-6 flex flex-col items-center text-center gap-3 hover:border-emerald-400/40 hover:bg-emerald-500/5 transition"
          >
            <Icon className="text-emerald-400" size={32} />
            <div className="font-semibold">{title}</div>
            <div className="text-white/40 text-sm">{desc}</div>
          </Link>
        ))}
      </div>
    </div>
  );
}