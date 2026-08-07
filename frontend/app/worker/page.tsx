"use client";

import { useEffect, useState } from "react";
import { Check, X } from "lucide-react";

const TARGET_QTY = 500;

export default function WorkerKiosk() {
  const [current, setCurrent] = useState(320);
  const [defect, setDefect] = useState(12);
  const [running, setRunning] = useState(false);
  const [rpm, setRpm] = useState(2500);
  const [temp, setTemp] = useState(42);
  const [showDefectModal, setShowDefectModal] = useState(false);
  const [elapsed, setElapsed] = useState(0); // seconds
  const [now, setNow] = useState(new Date());

  const defectReasons = ["절연저항 불량", "베어링 소음", "권선 단선", "축 편심", "기타"];

  // 실시간 시계
  useEffect(() => {
    const t = setInterval(() => setNow(new Date()), 1000);
    return () => clearInterval(t);
  }, []);

  // 설비 시뮬레이터: 5초마다 생산량 자동 증가 + RPM/온도 랜덤 변동
  useEffect(() => {
    if (!running) return;
    const t = setInterval(() => {
      setCurrent((c) => Math.min(c + 1, TARGET_QTY));
      setRpm(2400 + Math.round(Math.random() * 200));
      setTemp(38 + Math.round(Math.random() * 8));
      setElapsed((e) => e + 5);
    }, 5000);
    return () => clearInterval(t);
  }, [running]);

  const progress = Math.round((current / TARGET_QTY) * 100);

  const formatElapsed = (sec: number) => {
    const h = String(Math.floor(sec / 3600)).padStart(2, "0");
    const m = String(Math.floor((sec % 3600) / 60)).padStart(2, "0");
    const s = String(sec % 60).padStart(2, "0");
    return `${h}:${m}:${s}`;
  };

  const registerGood = () => setCurrent((c) => Math.min(c + 1, TARGET_QTY));

  const registerDefect = (reason: string) => {
    setDefect((d) => d + 1);
    setShowDefectModal(false);
  };

  return (
    <div className="min-h-screen bg-[#0b0f14] text-white p-6 text-[18px]">
      {/* 상단 바 */}
      <div className="flex items-center justify-between border-b border-white/10 pb-4 mb-6">
        <div className="text-emerald-400 font-bold text-xl">FactoryFlow — 작업자</div>
        <div className="text-white/70">
          작업자 <span className="text-white font-semibold">김철수</span> ｜ 권선 공정
        </div>
        <div className="font-mono">
          {now.toLocaleTimeString("ko-KR", { hour: "2-digit", minute: "2-digit" })}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-8">
        {/* 좌측: 현황 */}
        <div>
          <div className="text-white/40 text-sm mb-1">현재 LOT</div>
          <div className="text-blue-400 text-lg mb-4">LOT240626002</div>

          <div className="text-4xl font-bold mb-2">Motor-A</div>
          <span className="inline-block bg-emerald-500/20 text-emerald-400 px-3 py-1 rounded-md text-sm mb-6">
            {running ? "권선 공정 진행중" : "대기 중"}
          </span>

          <div className="space-y-5 border-t border-white/10 pt-6">
            <Row label="오늘 목표" value={`${TARGET_QTY} EA`} />
            <Row label="현재 실적" value={`${current} EA`} valueClass="text-emerald-400 text-3xl font-bold" />
            <Row label="불량" value={`${defect} EA`} valueClass="text-red-400 text-3xl font-bold" />
            <div>
              <div className="flex justify-between mb-2">
                <span className="text-white/60">달성률</span>
                <span className="text-emerald-400 font-semibold">{progress}%</span>
              </div>
              <div className="h-3 bg-white/10 rounded-full overflow-hidden">
                <div
                  className="h-full bg-emerald-400 transition-all duration-500"
                  style={{ width: `${progress}%` }}
                />
              </div>
            </div>
          </div>

          <div className="border-t border-white/10 mt-6 pt-4 space-y-2 text-white/70">
            <Row label="작업 시작" value="08:00" small />
            <Row label="경과 시간" value={formatElapsed(elapsed)} small mono />
          </div>
        </div>

        {/* 우측: 버튼 */}
        <div className="space-y-5">
          <button
            onClick={registerGood}
            className="w-full bg-[#0f1a14] border border-emerald-500/30 rounded-xl py-8 flex flex-col items-center gap-2 hover:bg-emerald-500/10 active:scale-[0.98] transition"
          >
            <Check className="text-emerald-400" size={40} />
            <span className="text-emerald-400 text-2xl font-bold">양품 등록</span>
            <span className="text-white/40 text-sm">정상 생산 완료 시 누르세요</span>
          </button>

          <button
            onClick={() => setShowDefectModal(true)}
            className="w-full bg-[#1a0f0f] border border-red-500/30 rounded-xl py-8 flex flex-col items-center gap-2 hover:bg-red-500/10 active:scale-[0.98] transition"
          >
            <X className="text-red-400" size={40} />
            <span className="text-red-400 text-2xl font-bold">불량 등록</span>
            <span className="text-white/40 text-sm">불량 발생 시 사유 선택</span>
          </button>

          <button
            onClick={() => setRunning((r) => !r)}
            className="w-full border border-white/10 rounded-xl py-6 text-white/40 hover:text-white/70 hover:border-white/20 transition"
          >
            <div className="font-semibold">{running ? "작업 일시정지" : "생산 시작"}</div>
            <div className="text-sm">LOT 마감 및 다음 공정 이관</div>
          </button>
        </div>
      </div>

      {/* 하단 상태바 */}
      <div className="fixed bottom-0 left-0 right-0 bg-[#0b0f14] border-t border-white/10 px-6 py-3 flex justify-between text-sm text-white/60">
        <span>라인 <b className="text-white">A-01</b></span>
        <span>설비 <b className="text-white">권선기 #3</b></span>
        <span>RPM <b className="text-white font-mono">{rpm}</b></span>
        <span>온도 <b className="text-white font-mono">{temp}°C</b></span>
        <span>목표잔량 <b className="text-white">{TARGET_QTY - current} EA</b></span>
      </div>

      {/* 불량 사유 모달 */}
      {showDefectModal && (
        <div className="fixed inset-0 bg-black/70 flex items-center justify-center z-50">
          <div className="bg-[#111823] border border-white/10 rounded-xl p-6 w-96">
            <div className="text-lg font-bold mb-4">불량 사유 선택</div>
            <div className="space-y-2">
              {defectReasons.map((r) => (
                <button
                  key={r}
                  onClick={() => registerDefect(r)}
                  className="w-full text-left px-4 py-3 rounded-lg bg-white/5 hover:bg-red-500/20 hover:text-red-400 transition"
                >
                  {r}
                </button>
              ))}
            </div>
            <button
              onClick={() => setShowDefectModal(false)}
              className="mt-4 w-full py-2 text-white/40 hover:text-white/70"
            >
              취소
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

function Row({
  label,
  value,
  valueClass = "text-white",
  small = false,
  mono = false,
}: {
  label: string;
  value: string;
  valueClass?: string;
  small?: boolean;
  mono?: boolean;
}) {
  return (
    <div className="flex justify-between items-baseline">
      <span className={small ? "text-white/50 text-sm" : "text-white/60"}>{label}</span>
      <span className={`${valueClass} ${mono ? "font-mono" : ""}`}>{value}</span>
    </div>
  );
}