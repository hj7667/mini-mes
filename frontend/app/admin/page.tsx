"use client";

import { LayoutDashboard, Factory, Search, Package, ShieldCheck, Truck } from "lucide-react";

const lots = [
  { id: "LOT240626001", qty: 500, status: "PASS" },
  { id: "LOT240626002", qty: 320, status: "진행중" },
  { id: "LOT240626002-ERR", qty: 12, status: "불량격리" },
  { id: "LOT240626003", qty: 200, status: "진행중" },
];

const stageProgress = [
  { name: "Motor-A 권선", done: 320, total: 500, color: "bg-emerald-400" },
  { name: "Motor-B 조립", done: 410, total: 500, color: "bg-amber-400" },
  { name: "Motor-C 검사", done: 225, total: 500, color: "bg-blue-400" },
];

const pipeline = [
  { name: "권선", qty: 320, status: "green" },
  { name: "조립", qty: 298, status: "green" },
  { name: "성능검사", qty: 271, status: "yellow" },
  { name: "포장", qty: 260, status: "green" },
  { name: "출하대기", qty: 101, status: "red" },
];

const statusStyle: Record<string, string> = {
  PASS: "bg-emerald-500/20 text-emerald-400",
  진행중: "bg-amber-500/20 text-amber-400",
  불량격리: "bg-red-500/20 text-red-400",
};

const dotStyle: Record<string, string> = {
  green: "bg-emerald-400",
  yellow: "bg-amber-400",
  red: "bg-red-400",
};

export default function AdminDashboard() {
  return (
    <div className="min-h-screen bg-[#0b0f14] text-white flex">
      {/* 사이드바 */}
      <aside className="w-52 border-r border-white/10 p-4 space-y-1">
        <div className="text-emerald-400 font-bold text-lg mb-6 px-2">
          FactoryFlow <span className="text-white/40 text-sm font-normal block">산업용 모터 MES</span>
        </div>
        {[
          { icon: LayoutDashboard, label: "대시보드", active: true },
          { icon: Factory, label: "생산관리" },
          { icon: Search, label: "LOT 추적" },
          { icon: Package, label: "재고관리" },
          { icon: ShieldCheck, label: "품질관리" },
          { icon: Truck, label: "출하관리" },
        ].map(({ icon: Icon, label, active }) => (
          <button
            key={label}
            className={`w-full flex items-center gap-3 px-3 py-2 rounded-lg text-sm transition ${
              active ? "bg-emerald-500/10 text-emerald-400" : "text-white/60 hover:bg-white/5"
            }`}
          >
            <Icon size={18} />
            {label}
          </button>
        ))}
      </aside>

      {/* 메인 */}
      <main className="flex-1 p-6">
        <div className="flex justify-between items-center mb-6 text-sm text-white/60">
          <div className="text-white/40">2026-06-26 09:42</div>
          <div>
            라인 가동률 <span className="text-emerald-400 font-semibold">94.2%</span>
          </div>
        </div>

        {/* KPI 카드 */}
        <div className="grid grid-cols-4 gap-4 mb-6">
          <Kpi label="오늘 생산" value="1,250 EA" valueClass="text-white" />
          <Kpi label="목표 달성률" value="64%" valueClass="text-blue-400" />
          <Kpi label="불량률" value="1.8%" valueClass="text-amber-400" />
          <Kpi label="비가동 알람" value="2건" valueClass="text-red-400" />
        </div>

        {/* 파이프라인 */}
        <div className="grid grid-cols-5 gap-4 mb-6">
          {pipeline.map((p, i) => (
            <div key={p.name} className="bg-[#111823] border border-white/10 rounded-xl p-4 relative">
              <div className="flex items-center gap-2 text-white/60 text-sm mb-2">
                <span className={`w-2 h-2 rounded-full ${dotStyle[p.status]}`} />
                {p.name}
              </div>
              <div className="text-2xl font-bold">{p.qty} EA</div>
              {i < pipeline.length - 1 && (
                <span className="absolute -right-3 top-1/2 -translate-y-1/2 text-white/20">→</span>
              )}
            </div>
          ))}
        </div>

        <div className="grid grid-cols-2 gap-4">
          {/* LOT 현황 */}
          <div className="bg-[#111823] border border-white/10 rounded-xl p-5">
            <div className="font-semibold mb-4">LOT 현황</div>
            <div className="space-y-3">
              {lots.map((l) => (
                <div key={l.id} className="flex justify-between items-center">
                  <span className="text-blue-400 text-sm">{l.id}</span>
                  <span className="text-white/70 text-sm">{l.qty} EA</span>
                  <span className={`text-xs px-2 py-1 rounded-md ${statusStyle[l.status]}`}>
                    {l.status}
                  </span>
                </div>
              ))}
            </div>
          </div>

          {/* 공정별 진행률 */}
          <div className="bg-[#111823] border border-white/10 rounded-xl p-5">
            <div className="font-semibold mb-4">공정별 진행률</div>
            <div className="space-y-4">
              {stageProgress.map((s) => (
                <div key={s.name}>
                  <div className="flex justify-between text-sm mb-1">
                    <span>{s.name}</span>
                    <span className="text-white/60">
                      {s.done} / {s.total}
                    </span>
                  </div>
                  <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                    <div
                      className={`h-full ${s.color}`}
                      style={{ width: `${(s.done / s.total) * 100}%` }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

function Kpi({ label, value, valueClass }: { label: string; value: string; valueClass: string }) {
  return (
    <div className="bg-[#111823] border border-white/10 rounded-xl p-5">
      <div className="text-white/50 text-sm mb-1">{label}</div>
      <div className={`text-2xl font-bold ${valueClass}`}>{value}</div>
    </div>
  );
}