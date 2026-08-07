"use client";

import { useState } from "react";
import { Search, AlertTriangle } from "lucide-react";

const timeline = [
  { time: "08:00", title: "생산지시 발령", status: "READY", detail: "담당: 관리자 이영희 ｜ 지시수량: 500 EA", color: "border-blue-400" },
  { time: "08:05", title: "권선", status: "PASS", detail: "작업자: 김철수 ｜ 설비: 권선기 #3 ｜ 실적: 500 EA ｜ 불량: 0 EA ｜ 소요시간 01:12:00", color: "border-emerald-400" },
  { time: "09:17", title: "조립", status: "PASS", detail: "작업자: 박민준 ｜ 설비: 조립라인 #1 ｜ 실적: 500 EA ｜ 불량: 0 EA ｜ 소요시간 00:53:00", color: "border-emerald-400" },
  { time: "10:10", title: "성능검사", status: "FAIL 발생", detail: "검사원: 최지훈 ｜ 검사항목: 절연저항 ｜ 양품: 488 EA ｜ 불량: 12 EA (절연파괴)", extra: "→ LOT240626002-ERR 자동 분할 생성", color: "border-red-400" },
  { time: "10:55", title: "포장", status: "진행중", detail: "작업자: 이수연 ｜ 대상: 양품 488 EA ｜ 현재: 320 EA 완료 ｜ 경과 00:47:00", color: "border-emerald-400" },
  { time: "-", title: "출하 대기", status: "PENDING", detail: "포장 완료 후 자동 이관", color: "border-white/20" },
];

const statusColor: Record<string, string> = {
  READY: "bg-blue-500/20 text-blue-400",
  PASS: "bg-emerald-500/20 text-emerald-400",
  "FAIL 발생": "bg-red-500/20 text-red-400",
  진행중: "bg-amber-500/20 text-amber-400",
  PENDING: "bg-white/10 text-white/40",
};

export default function LotTraceViewer() {
  const [query, setQuery] = useState("LOT240626002");

  return (
    <div className="min-h-screen bg-[#0b0f14] text-white p-6">
      <div className="flex justify-between items-center mb-6">
        <div className="text-emerald-400 font-bold">FactoryFlow — LOT 추적</div>
        <div className="text-white/40 text-sm">Traceability Viewer</div>
      </div>

      <div className="flex gap-4 mb-6">
        <div className="flex-1 relative">
          <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-white/30" size={18} />
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="w-full bg-[#111823] border border-white/10 rounded-lg pl-11 pr-4 py-3 outline-none focus:border-emerald-400/50"
            placeholder="LOT 번호 입력"
          />
        </div>
        <div className="bg-[#111823] border border-white/10 rounded-lg px-6 py-3 flex gap-8 text-sm">
          <MiniStat label="공정" value="조립" />
          <MiniStat label="제품" value="Motor-A" />
          <MiniStat label="목표수량" value="500 EA" />
          <MiniStat label="생성일" value="2026-06-26" />
        </div>
      </div>

      <div className="grid grid-cols-3 gap-6">
        {/* 공정 이력 타임라인 */}
        <div className="col-span-2 bg-[#111823] border border-white/10 rounded-xl p-6">
          <div className="text-white/60 text-sm mb-5">공정 이력</div>
          <div className="space-y-6">
            {timeline.map((t, i) => (
              <div key={i} className="flex gap-4">
                <div className="flex flex-col items-center">
                  <div className={`w-3 h-3 rounded-full border-2 ${t.color} bg-[#0b0f14]`} />
                  {i < timeline.length - 1 && <div className="w-px flex-1 bg-white/10 mt-1" />}
                </div>
                <div className="pb-2">
                  <div className="text-white/40 text-xs mb-1">{t.time}</div>
                  <div className="flex items-center gap-2 mb-1">
                    <span className="font-semibold">{t.title}</span>
                    <span className={`text-xs px-2 py-0.5 rounded-md ${statusColor[t.status]}`}>
                      {t.status}
                    </span>
                  </div>
                  <div className="text-white/50 text-sm">{t.detail}</div>
                  {t.extra && (
                    <div className="text-red-400 text-sm mt-1">{t.extra}</div>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* 우측 요약 패널 */}
        <div className="space-y-4">
          <div className="bg-[#111823] border border-white/10 rounded-xl p-5">
            <div className="text-white/60 text-sm mb-4">LOT 요약</div>
            <div className="space-y-3 text-sm">
              <Row label="LOT 번호" value="LOT240626002" valueClass="text-blue-400" />
              <Row label="지시수량" value="500 EA" />
              <Row label="양품수량" value="488 EA" valueClass="text-emerald-400" />
              <Row label="불량수량" value="12 EA" valueClass="text-red-400" />
              <Row label="불량률" value="2.4%" valueClass="text-amber-400" />
              <Row label="현재 공정" value="포장" />
              <Row label="상태" value="RUNNING" valueClass="text-blue-400" />
            </div>
          </div>

          <div className="bg-red-500/10 border border-red-500/30 rounded-xl p-4">
            <div className="flex items-center gap-2 text-red-400 font-semibold mb-2">
              <AlertTriangle size={16} /> LOT 분할 발생
            </div>
            <div className="text-white/60 text-sm">
              성능검사 불량 12 EA 발생으로 LOT240626002-ERR 자동 생성. 격리창고 입고 처리됨.
            </div>
          </div>

          <div className="bg-[#111823] border border-white/10 rounded-xl p-5">
            <div className="text-white/60 text-sm mb-3">재고 반영 현황</div>
            <div className="space-y-2 text-sm">
              <Row label="완제품 창고" value="320 EA" />
              <Row label="포장 진행중" value="168 EA" />
              <Row label="격리 창고" value="12 EA" valueClass="text-red-400" />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

function Row({ label, value, valueClass = "text-white" }: { label: string; value: string; valueClass?: string }) {
  return (
    <div className="flex justify-between">
      <span className="text-white/50">{label}</span>
      <span className={valueClass}>{value}</span>
    </div>
  );
}

function MiniStat({ label, value }: { label: string; value: string }) {
  return (
    <div>
      <div className="text-white/40 text-xs">{label}</div>
      <div className="font-semibold">{value}</div>
    </div>
  );
}